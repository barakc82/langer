package com.langer.server.service.statistics;

import com.langer.server.model.repository.queryitems.SentenceWithLocation;
import com.langer.server.service.SentencePart;

import java.util.List;

public interface CorpusStatisticsCalculatorParent
{
    List<SentencePart> extractSentenceParts(SentenceWithLocation sentence, long languageId);
}