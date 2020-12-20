package com.langer.server.api.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindSentenceRequest
{
    String  languageName;
    String  resourceTitle;
    String  episodeTitle;
    int     paragraphIndex;
    int     sentenceIndex;
}
