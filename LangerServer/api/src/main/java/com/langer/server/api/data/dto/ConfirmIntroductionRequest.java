package com.langer.server.api.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public
class ConfirmIntroductionRequest
{
    long wordTranslationId;
}
