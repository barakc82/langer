package com.langer.server.api.data.dto;

import com.langer.server.api.data.dto.TranslationIntroductionCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card
{
    TranslationIntroductionCard translationIntroductionCard;
}