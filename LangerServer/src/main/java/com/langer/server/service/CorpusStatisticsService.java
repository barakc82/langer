package com.langer.server.service;

import com.langer.server.api.admin.dto.*;
import com.langer.server.model.entity.impl.Language;
import com.langer.server.model.repository.queryitems.SentenceWithLocation;
import com.langer.server.service.importancecalculators.ImportanceCalculatorFactory;
import com.langer.server.service.statistics.CorpusStatisticsCalculator;
import com.langer.server.service.statistics.CorpusStatisticsCalculatorParent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class CorpusStatisticsService implements CorpusStatisticsCalculatorParent
{
    private final TextStorage textStorage;
    private final ImportanceCalculatorFactory importanceCalculatorFactory;

    public CorpusStatisticsResponse calculateCorpusStatistics(Language language)
    {
        ImportanceCalculator importanceCalculator = importanceCalculatorFactory.create(language.getName());
        CorpusStatisticsCalculator corpusStatisticsCalculator = new CorpusStatisticsCalculator(this, language.getId(), importanceCalculator);

        List<SentenceWithLocation> sentences = textStorage.findSentences(language.getId());

        return corpusStatisticsCalculator.calculateCorpusStatistics(sentences);
    }

    @Override
    public List<SentencePart> extractSentenceParts(SentenceWithLocation sentence, long languageId)
    {
        return textStorage.calculateSentenceParts(sentence.getSentenceId(), sentence.getSentenceText(), languageId);
    }
}