package com.langer.server.service;

import com.langer.server.api.admin.dto.WordOccurrenceDto;
import com.langer.server.api.admin.dto.WordTranslationDto;
import com.langer.server.model.entity.impl.Word;
import lombok.Data;

@Data
public class SentencePart
{
    private String              originalWordValue;
    private WordOccurrenceDto   wordOccurrence;
}
