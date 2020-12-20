package com.langer.server.api.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLanguageProgress
{
    String                            languageName;
    List<WordTranslationUserStateDto> wordTranslationUserStates;
}