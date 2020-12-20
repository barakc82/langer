package com.langer.server.service;


import com.langer.server.api.admin.dto.CorpusStatisticsResponse;
import com.langer.server.api.admin.dto.SentenceStatistics;
import com.langer.server.api.admin.dto.WordTranslationStatistics;
import com.langer.server.model.entity.impl.Language;
import com.langer.server.model.repository.LanguageRepository;
import com.langer.server.sentencegraph.SentenceGraph;
import com.langer.server.sentencegraph.SentenceNode;
import com.langer.server.sentencegraph.TranslationNode;
import com.langer.server.sentencegraph.TranslationToSentenceMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SentenceGraphService
{
    private final TextStorage               textStorage;
    private final LanguageRepository        languageRepository;
    private final CorpusStatisticsService   corpusStatisticsService;

    private final Map<Long, SentenceGraph>  languageIdToSentenceGraph = new HashMap<>();

    @PostConstruct
    void buildAllGraphs()
    {
        List<Language> languages = languageRepository.findAll();
        for (Language language : languages)
        {
            SentenceGraph graph = buildGraph(language);
            languageIdToSentenceGraph.put(language.getId(), graph);
        }
    }

    SentenceGraph buildGraph(Language language)
    {
        long startTime = System.currentTimeMillis();
        TranslationToSentenceMapping wordSentencesMapping = textStorage.getTranslationToSentenceMapping(language.getId());

        long translationToSentencesMappingDone = measureTime("Translation to sentences mapping", startTime);

        CorpusStatisticsResponse corpusStatisticsResponse = corpusStatisticsService.calculateCorpusStatistics(language);

        long corpusStatisticsCalculationDone = measureTime("Corpus statistics calculation", translationToSentencesMappingDone);

        for (WordTranslationStatistics wordTranslationStatistics : corpusStatisticsResponse.getWordTranslationStatisticsItems())
        {
            TranslationNode translationNode = wordSentencesMapping.getTranslationNode(wordTranslationStatistics.getWordTranslationId());
            translationNode.setRanking(wordTranslationStatistics.getInverseFrequency());
        }

        for (SentenceStatistics sentenceStatistics : corpusStatisticsResponse.getSentenceStatisticsItems())
        {
            SentenceNode sentenceNode = wordSentencesMapping.getSentenceNode(sentenceStatistics.getId());
            sentenceNode.setRanking(sentenceStatistics.getRanking());
        }

        long rankingDone = measureTime("Ranking", corpusStatisticsCalculationDone);

        SentenceGraph sentenceGraph = new SentenceGraph();
        for (SentenceNode curSentenceNode : wordSentencesMapping.getSentenceNodes())
        {
            if (curSentenceNode.getRanking() == 0)
                continue;

            Set<TranslationNode> curWordNodes = curSentenceNode.getWordNodes();
            int currentSentenceRanking = curSentenceNode.getRanking();
            for (TranslationNode wordNode : curWordNodes)
            {
                Set<SentenceNode> sentenceCandidates = wordNode.getSentenceNodes();
                for (SentenceNode sentenceCandidate : sentenceCandidates)
                {
                    if (curSentenceNode == sentenceCandidate || sentenceCandidate.getRanking() == 0)
                        continue;

                    boolean isCandidateContainsCurrent = sentenceCandidate.getWordNodes().containsAll(curWordNodes);
                    boolean isCurrentContainsCandidate = curWordNodes.containsAll(sentenceCandidate.getWordNodes());

                    if (isCandidateContainsCurrent && isCurrentContainsCandidate)
                        continue;

                    Set<TranslationNode> candidateSentenceWords = new HashSet<>(sentenceCandidate.getWordNodes());
                    candidateSentenceWords.removeAll(curWordNodes);

                    int candidateRanking = sentenceCandidate.getRanking();

                    if (candidateSentenceWords.size() == 1 && currentSentenceRanking < candidateRanking)
                        sentenceGraph.addDirectedEdge(curSentenceNode, sentenceCandidate);
                }
            }
            sentenceGraph.addNode(curSentenceNode);
        }

        long graphBuildDone = measureTime("Graph build", rankingDone);

        Set<SentenceNode> sourceNodeSet = sentenceGraph.getSentenceNodes().stream().collect(Collectors.toSet());
        for (SentenceNode sentenceNode : wordSentencesMapping.getSentenceNodes())
            sourceNodeSet.removeAll(sentenceNode.getSentenceNeighbors());

        List<SentenceNode> sourceNodes = new ArrayList<>(sourceNodeSet);
        sourceNodes.sort(Comparator.comparing(SentenceNode::getRanking));
        sentenceGraph.setSourceNodes(sourceNodes);

        measureTime("Sources calculation", graphBuildDone);

        log.info(language.getName() + ", " + sentenceGraph.getSourceNodes().size() + " out of " + wordSentencesMapping.getSentenceNodes().size());
        if (!sourceNodes.isEmpty())
            log.info(language.getName() + ", sentence " + sourceNodes.get(0).getSentenceId() + " with ranking " + sourceNodes.get(0).getRanking());
        return sentenceGraph;
    }

    private long measureTime(String stepName, long previousStepDone)
    {
        long curStepDone = System.currentTimeMillis();
        long delta = curStepDone - previousStepDone;
        log.info('\t' + stepName + " took " + delta/1000 + " seconds");
        return curStepDone;
    }

    public TranslationNode getNextTargetWord(long languageId, Set<Long> knownWordIds)
    {
        SentenceGraph sentenceGraph = languageIdToSentenceGraph.get(languageId);

        Optional<TranslationNode> lowestRankingWord = sentenceGraph.getSourceNodes().stream()
                .map(sourceNode -> getNextTargetWord(sourceNode, knownWordIds))
                .filter(word -> word != null)
                .min(Comparator.comparing(TranslationNode::getRanking));

        Optional<ImmutablePair<SentenceNode, Integer>> sourceAndRanking = sentenceGraph.getSourceNodes().stream()
                .filter(source -> source.getWordNodes().stream().anyMatch(translationNode -> !knownWordIds.contains(translationNode.getWordTranslationId())))
                .map(source -> ImmutablePair.of(source, calculateUnknownWordsRankingSum(source.getWordNodes(), knownWordIds)))
                .min(Comparator.comparing(ImmutablePair::getValue));

        int lowestSingleWordRank        = lowestRankingWord.isPresent() ? lowestRankingWord.get().getRanking() : Integer.MAX_VALUE;
        int lowestWordSetCombinedRank   = sourceAndRanking.isPresent() ? sourceAndRanking.get().getValue() : Integer.MAX_VALUE;

        System.out.println("barak: lowestRankingWord: " + lowestRankingWord.get().getWordTranslationId());
        System.out.println("barak: option 1: " + lowestSingleWordRank);
        System.out.println("barak: option 2: " + lowestWordSetCombinedRank);

        if (lowestSingleWordRank <= lowestWordSetCombinedRank)
            return lowestRankingWord.get();

        SentenceNode lowestWordSetSource = sourceAndRanking.get().getKey();

        for (TranslationNode translationNode : lowestWordSetSource.getWordNodes())
            System.out.println("barak: translationNode in lowestWordSetSource: " + translationNode.getWordTranslationId() + " " + translationNode.getRanking());

        lowestRankingWord = lowestWordSetSource.getWordNodes().stream()
                .filter(translation -> !knownWordIds.contains(translation.getWordTranslationId()))
                .min(Comparator.comparing(TranslationNode::getRanking));

        System.out.println("barak: lowestRankingSource: " + lowestRankingWord.get().getWordTranslationId());
        return lowestRankingWord.get();
    }

    private int calculateUnknownWordsRankingSum(Set<TranslationNode> translationNodes, Set<Long> knownWordIds)
    {
        return translationNodes.stream()
                .filter(translationNode -> !knownWordIds.contains(translationNode.getWordTranslationId()))
                .map(TranslationNode::getRanking)
                .collect(Collectors.summingInt(Integer::intValue));
    }

    private TranslationNode getNextTargetWord(SentenceNode sentenceNode, Set<Long> knownWordIds)
    {
        Set<TranslationNode> sentenceWords = new HashSet<>(sentenceNode.getWordNodes());
        sentenceWords.removeAll(knownWordIds);
        if (sentenceWords.size() == 1)
        {
            TranslationNode wordToLearn = sentenceWords.stream().findAny().get();
            System.out.println("barak: getNextTargetWord, sentence " + sentenceNode.getSentenceId() + " matches, word ID " + wordToLearn.getWordTranslationId() + ", ranking " + wordToLearn.getRanking());
            return sentenceWords.stream().findAny().get();
        }

        if (sentenceWords.size() > 1)
        {
            System.out.println("barak: getNextTargetWord, sentence " + sentenceNode.getSentenceId() + " too many unknown words");
            return null;
        }

        System.out.println("barak: getNextTargetWord, sentence " + sentenceNode.getSentenceId() + " already known, checking children");
        Optional<TranslationNode> lowestRankingWord = sentenceNode.getSentenceNeighbors().stream()
                .map(child -> getNextTargetWord(child, knownWordIds))
                .filter(word -> word != null)
                .min(Comparator.comparing(TranslationNode::getRanking));

        return lowestRankingWord.get();
    }
}