package com.langer.server.api.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class CorpusStatisticsResponse
{
    private List<WordTranslationStatistics> wordTranslationStatisticsItems;
    private List<SentenceStatistics>    sentenceStatisticsItems;
}
