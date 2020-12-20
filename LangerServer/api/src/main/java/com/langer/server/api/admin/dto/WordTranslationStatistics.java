package com.langer.server.api.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class WordTranslationStatistics
{
    private long    wordTranslationId;
    private long    wordId;
    private String  wordValue;
    private String  translation;
    private int     count;
    private int     inverseFrequency;
}
