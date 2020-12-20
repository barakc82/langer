package com.langer.server.api.data;

import com.langer.server.api.admin.dto.LanguageDto;
import com.langer.server.api.admin.dto.NextCardRequest;
import com.langer.server.api.data.dto.Card;
import com.langer.server.api.data.dto.ConfirmIntroductionRequest;
import com.langer.server.model.entity.impl.Language;
import com.langer.server.model.repository.LanguageRepository;
import com.langer.server.service.CardFactory;
import com.langer.server.service.LangerModelMapper;
import com.langer.server.service.UserProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DataController implements DataApi
{
    private final LanguageRepository    languageRepository;
    private final LangerModelMapper     modeler;
    private final CardFactory           cardFactory;
    private final UserProgressService   userProgressService;

    @Override
    public List<LanguageDto> getLanguages()
    {
        return languageRepository.findAll().stream().map(language -> modeler.toDto(language)).collect(Collectors.toList());
    }

    @Override
    public Card getNextCard(NextCardRequest nextCardRequest)
    {
        Language language = languageRepository.findByName(nextCardRequest.getLanguageName());
        return cardFactory.create(language.getId());
    }

    @Override
    public Card confirmIntroduction(ConfirmIntroductionRequest confirmIntroductionRequest)
    {
        userProgressService.confirmIntroduction(confirmIntroductionRequest.getWordTranslationId());
        Language language = languageRepository.findByWordTranslationId(confirmIntroductionRequest.getWordTranslationId());
        return cardFactory.create(language.getId());
    }
}