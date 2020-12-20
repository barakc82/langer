package com.langer.server.model.entity;

import com.langer.server.api.admin.dto.WordType;

public interface WordTranslationForStorage
{
    long        getWordId();
    String      getWordValue();
    WordType    getWordType();
    long        getLanguageId();
    long        getWordTranslationId();
    String      getTranslation();
}