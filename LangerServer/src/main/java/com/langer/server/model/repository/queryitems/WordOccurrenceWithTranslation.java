package com.langer.server.model.repository.queryitems;

import com.langer.server.api.admin.dto.WordType;

public interface WordOccurrenceWithTranslation
{
    long        getWordId();

    String      getWordValue();

    WordType    getWordType();

    int         getWordPosition();

    long        getWordTranslationId();

    String      getWordTranslation();

    long        getWordOccurrenceId();

    long        getSentenceId();
}