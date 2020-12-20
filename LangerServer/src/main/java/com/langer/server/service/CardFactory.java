package com.langer.server.service;

import com.langer.server.api.admin.dto.WordDto;
import com.langer.server.api.admin.dto.WordWithTranslation;
import com.langer.server.api.data.dto.Card;
import com.langer.server.api.data.dto.TranslationIntroductionCard;
import com.langer.server.model.entity.impl.ProgressState;
import com.langer.server.model.entity.impl.WordTranslationUserState;
import com.langer.server.sentencegraph.TranslationNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardFactory
{
    private final SentenceGraphService  sentenceGraphService;
    private final TextStorage           textStorage;
    private final UserProgressService   userProgressService;

    public Card create(long languageId)
    {
        List<WordTranslationUserState> alreadyIntroducedTranslations = userProgressService.getAllAlreadyIntroducedTranslations(0);
        Set<Long> alreadyIntroducedTranslationIds = alreadyIntroducedTranslations.stream().map(WordTranslationUserState::getWordTranslationId).collect(Collectors.toSet());
        TranslationNode     nextTargetWord      = sentenceGraphService.getNextTargetWord(languageId, alreadyIntroducedTranslationIds);
        long                wordTranslationId   = nextTargetWord.getWordTranslationId();

        WordWithTranslation wordWithTranslation = textStorage.getWordWithTranslation(wordTranslationId);
        WordDto             word                = wordWithTranslation.getWord();

        System.out.println("barak: next target word is " + word.getValue() + " (" + word.getId() + ")");

        TranslationIntroductionCard translationIntroductionCard = TranslationIntroductionCard.builder()
                .wordTranslationId(wordTranslationId)
                .source(word.getValue())
                .translation(wordWithTranslation.getWordTranslation().getTranslation())
                .build();

        return Card.builder().translationIntroductionCard(translationIntroductionCard).build();
    }
}