package com.langer.server.api.admin.dto;

import lombok.Data;

@Data
public class CreateWordTranslationRequest
{
    private long    wordId;
    private String  translation;
}
