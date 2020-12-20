package com.langer.server.model.repository.queryitems;

import com.langer.server.api.admin.dto.WordType;
import com.langer.server.model.entity.impl.ProgressState;

import javax.persistence.Column;

public interface WordTranslationUserStateQueryItem
{
    String          getLanguageName();
    String          getWordValue();
    String          getWordTranslation();
    ProgressState   getProgressState();
    int             getProgressCounter();
}