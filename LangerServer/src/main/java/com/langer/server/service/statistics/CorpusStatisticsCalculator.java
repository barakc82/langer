package com.langer.server.service.statistics;

import com.langer.server.api.admin.dto.*;
import com.langer.server.model.repository.queryitems.SentenceWithLocation;
import com.langer.server.service.ImportanceCalculator;
import com.langer.server.service.SentencePart;
import com.langer.server.util.TextUtils;

import java.util.*;
import java.util.stream.Collectors;

public class CorpusStatisticsCalculator
{
    private final CorpusStatisticsCalculatorParent parent;
    private final long languageId;
    private final ImportanceCalculator importanceCalculator;

    private final Map<WordWithTranslation, Integer> wordTranslationCount            = new HashMap<>();
    private final Map<WordWithTranslation, WordTranslationStatistics> wordTranslationStatisticsMap    = new HashMap<>();
    private final Map<SentenceWithLocation, Integer>                  sentenceRankings                = new HashMap<>();
    private final Map<SentenceWithLocation, List<SentencePart>>       sentenceToSentenceParts         = new HashMap<>();
    private final Set<String> rankedSentenceTexts             = new HashSet<>();


    public CorpusStatisticsCalculator(CorpusStatisticsCalculatorParent corpusStatisticsCalculatorParent, long languageId,
                                        ImportanceCalculator importanceCalculator)
    {
        this.parent               = corpusStatisticsCalculatorParent;
        this.languageId           = languageId;
        this.importanceCalculator = importanceCalculator;
    }

    private void processSentence(SentenceWithLocation sentence, long languageId)
    {
        List<SentencePart> sentenceParts = parent.extractSentenceParts(sentence, languageId);
/*
        sentenceParts.forEach(sentencePart ->
        {
            if (sentencePart.getWordOccurrence() == null)
                return;

            if (sentencePart.getWordOccurrence().getWord().getType() == WordType.Name)
                return;

            if (sentencePart.getWordOccurrence().getWord().getTranslations() != null)
                return;

            WordTranslationDto emptyWordTranslation = new WordTranslationDto();
            emptyWordTranslation.setTranslation("");
            sentencePart.getWordOccurrence().getWord().setTranslations(Arrays.asList(emptyWordTranslation));
        });
*/
        sentenceToSentenceParts.put(sentence, sentenceParts);

        sentenceParts.forEach(sentencePart ->
        {
            if (sentencePart.getWordOccurrence() == null)
                return;

            if (sentencePart.getWordOccurrence().getWord().getType() == WordType.Name)
                return;

            WordWithTranslation wordWithTranslation = WordWithTranslation.builder()
                    .word(sentencePart.getWordOccurrence().getWord())
                    .wordTranslation(sentencePart.getWordOccurrence().getWordTranslation())
                    .build();

            Integer count = wordTranslationCount.get(wordWithTranslation);
            if (count == null)
                count = 0;

            wordTranslationCount.put(wordWithTranslation, ++count);
        });
    }

    private void addSentenceStatistics(SentenceWithLocation sentence)
    {
        if (sentence.getShouldBeIgnored())
            return;

        if (sentenceToSentenceParts.get(sentence) == null)
            return;

        if (rankedSentenceTexts.contains(TextUtils.cleanSentence(sentence.getSentenceText())))
        {
            return;
        }

        List<SentencePart> sentenceParts = sentenceToSentenceParts.get(sentence);
        if (!importanceCalculator.isInterestingSentence(sentenceParts))
            return;

        int sum = sentenceParts.stream()
                .filter(sentencePart -> sentencePart.getWordOccurrence() != null)
                .map(sentencePart ->
                {
                    if (sentencePart.getWordOccurrence().getWord().getType() == WordType.Name)
                        return 0;

                    WordWithTranslation wordWithTranslation = WordWithTranslation.builder()
                            .word(sentencePart.getWordOccurrence().getWord())
                            .wordTranslation(sentencePart.getWordOccurrence().getWordTranslation())
                            .build();
                    WordTranslationStatistics wordTranslationStatistics = wordTranslationStatisticsMap.get(wordWithTranslation);
                    if (wordTranslationStatistics == null)
                        return 0;

                    return wordTranslationStatistics.getInverseFrequency();
                })
                .collect(Collectors.summingInt(Integer::intValue));

        sentenceRankings.put(sentence, sum);
        rankedSentenceTexts.add(TextUtils.cleanSentence(sentence.getSentenceText()));
    }

    private List<Map.Entry<WordWithTranslation, Integer>> extractSortedWordTranslationCount()
    {
        List<Map.Entry<WordWithTranslation, Integer>> list = new ArrayList<>(wordTranslationCount.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        return list;
    }

    private List<WordTranslationStatistics> calculateWordTranslationStatistics()
    {
        List<Map.Entry<WordWithTranslation, Integer>> wordTranslationCountEntries = extractSortedWordTranslationCount();
        if (wordTranslationCountEntries.isEmpty())
            return Collections.emptyList();

        int maxWordCount = wordTranslationCountEntries.stream()
                .map(translationCountEntry -> translationCountEntry.getValue())
                .max(Integer::compare).get();

        List<WordTranslationStatistics> wordTranslationStatisticsItems = wordTranslationCountEntries.stream().map(translationCountEntry ->
        {
            WordTranslationStatistics wordTranslationStatistics = new WordTranslationStatistics();
            wordTranslationStatistics.setWordTranslationId(translationCountEntry.getKey().getWordTranslation().getId());
            wordTranslationStatistics.setWordId(translationCountEntry.getKey().getWord().getId());
            wordTranslationStatistics.setWordValue(translationCountEntry.getKey().getWord().getValue());
            wordTranslationStatistics.setTranslation(translationCountEntry.getKey().getWordTranslation().getTranslation());
            wordTranslationStatistics.setCount(translationCountEntry.getValue());
            wordTranslationStatistics.setInverseFrequency(maxWordCount / translationCountEntry.getValue());

            wordTranslationStatisticsMap.put(translationCountEntry.getKey(), wordTranslationStatistics);

            return wordTranslationStatistics;

        }).collect(Collectors.toList());
        return wordTranslationStatisticsItems;
    }

    public CorpusStatisticsResponse calculateCorpusStatistics(List<SentenceWithLocation> sentences)
    {
        sentences.forEach(sentence -> processSentence(sentence, languageId));
        List<WordTranslationStatistics> wordTranslationStatisticsItems = calculateWordTranslationStatistics();

        sentences.forEach(sentence -> addSentenceStatistics(sentence));
        List<SentenceStatistics>    sentenceStatisticsItems = calculateSentenceStatistics();

        CorpusStatisticsResponse corpusStatisticsResponse = new CorpusStatisticsResponse();
        corpusStatisticsResponse.setWordTranslationStatisticsItems(wordTranslationStatisticsItems);
        corpusStatisticsResponse.setSentenceStatisticsItems(sentenceStatisticsItems);
        return corpusStatisticsResponse;
    }

    private List<SentenceStatistics> calculateSentenceStatistics()
    {
        List<Map.Entry<SentenceWithLocation, Integer>> sentenceRankingEntries = new ArrayList<>(sentenceRankings.entrySet());
        sentenceRankingEntries.sort(Map.Entry.comparingByValue());

        List<SentenceStatistics> sentenceStatisticsItems = sentenceRankingEntries.stream().map(sentenceRankingEntry ->
        {
            SentenceWithLocation sentenceWithLocation = sentenceRankingEntry.getKey();
            SentenceStatistics sentenceStatistics = new SentenceStatistics();
            sentenceStatistics.setId(sentenceWithLocation.getSentenceId());
            sentenceStatistics.setSentence(sentenceWithLocation.getSentenceText());
            sentenceStatistics.setRanking(sentenceRankingEntry.getValue());
            sentenceStatistics.setLanguageName(sentenceWithLocation.getLanguageName());
            sentenceStatistics.setResourceTitle(sentenceWithLocation.getResourceTitle());
            sentenceStatistics.setEpisodeTitle(sentenceWithLocation.getEpisodeTitle());
            sentenceStatistics.setParagraphIndex(sentenceWithLocation.getParagraphIndex());
            sentenceStatistics.setSentenceIndex(sentenceWithLocation.getSentenceIndex());
            sentenceStatistics.setTranslation(sentenceWithLocation.getTranslation());
            return sentenceStatistics;

        }).collect(Collectors.toList());

        return sentenceStatisticsItems;
    }
}