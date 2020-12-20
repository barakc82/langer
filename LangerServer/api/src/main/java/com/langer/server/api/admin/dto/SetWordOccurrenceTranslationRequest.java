package com.langer.server.api.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetWordOccurrenceTranslationRequest
{
    long wordTranslationId;
    long sentenceId;
    int  wordPosition;
}