package com.langer.server.service;

import com.langer.server.api.admin.dto.*;
import com.langer.server.model.entity.impl.*;
import org.springframework.stereotype.Service;

@Service
public class LangerModelMapper
{
    public LanguageDto toDto(Language language)
    {
        return LanguageDto.builder()
                .id(language.getId())
                .name(language.getName())
                .build();
    }

    public ResourceDto toDto(Resource resource)
    {
        return ResourceDto.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .languageId(resource.getLanguageId())
                .build();
    }

    public EpisodeDto toDto(Episode episode)
    {
        return EpisodeDto.builder()
                .id(episode.getId())
                .title(episode.getTitle())
                .text(episode.getText())
                .resourceId(episode.getResourceId())
                .build();
    }

    public WordDto toDto(Word word)
    {
        return WordDto.builder()
                .id(word.getId())
                .value(word.getValue())
                .type(word.getType())
                .build();
    }

    public WordTranslationDto toDto(WordTranslation wordTranslation)
    {
        return WordTranslationDto.builder()
                .id(wordTranslation.getId())
                .translation(wordTranslation.getTranslation())
                .build();
    }
}