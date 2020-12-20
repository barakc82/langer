package com.langer.server.api.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeDto
{
    private long    id;
    private String  title;
    private String  text;
    private long    languageId;
    private long    resourceId;
}