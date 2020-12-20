package com.langer.server.api.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SentenceDto
{
    private long                    id;
    private String                  text;
    private List<WordOccurrenceDto> wordOccurrences;
    private String                  languageName;
    private String                  resourceTitle;
    private String                  episodeTitle;
    private String                  translation;
}