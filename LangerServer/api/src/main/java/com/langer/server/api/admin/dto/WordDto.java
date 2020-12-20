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
public class WordDto
{
    private long                        id;
    private String                      value;
    private List<WordTranslationDto>    translations;
    private WordType type;
}