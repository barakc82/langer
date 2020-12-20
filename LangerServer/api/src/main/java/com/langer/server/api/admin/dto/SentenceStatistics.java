package com.langer.server.api.admin.dto;

import lombok.Data;

@Data
public class SentenceStatistics
{
    private long    id;
    private String  sentence;
    private int     count;
    private int     ranking;
    private String  languageName;
    private String  resourceTitle;
    private String  episodeTitle;
    private int     paragraphIndex;
    private int     sentenceIndex;
    private String  translation;
}
