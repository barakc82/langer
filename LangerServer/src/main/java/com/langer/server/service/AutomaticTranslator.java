package com.langer.server.service;

import com.langer.server.api.admin.dto.WordOccurrenceDto;

import java.util.List;

public interface AutomaticTranslator
{
    List<WordOccurrenceDto> translate(List<SentencePart> sentenceParts);
}