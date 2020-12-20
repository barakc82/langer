package com.langer.server.service;

import com.langer.server.api.data.dto.UserLanguageProgress;
import com.langer.server.api.data.dto.WordTranslationUserStateDto;
import com.langer.server.model.entity.impl.ProgressState;
import com.langer.server.model.entity.impl.WordTranslationUserState;
import com.langer.server.model.repository.WordTranslationUserStateRepository;
import com.langer.server.model.repository.queryitems.WordTranslationUserStateQueryItem;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProgressService
{
    private final WordTranslationUserStateRepository wordTranslationUserStateRepository;

    public List<WordTranslationUserState> getAllAlreadyIntroducedTranslations(long userId)
    {
        List<WordTranslationUserState> alreadyIntroducedTranslations = wordTranslationUserStateRepository.findByUserIdAndProgressState(userId, ProgressState.INTRODUCED);
        return alreadyIntroducedTranslations;
    }

    public void confirmIntroduction(long wordTranslationId)
    {
        WordTranslationUserState wordTranslationUserState = wordTranslationUserStateRepository.findByUserIdAndWordTranslationId(0, wordTranslationId);
        Assert.isTrue(wordTranslationUserState == null, "User has already been introduced to this translation");

        wordTranslationUserState = new WordTranslationUserState();
        wordTranslationUserState.setUserId(0);
        wordTranslationUserState.setWordTranslationId(wordTranslationId);
        wordTranslationUserState.setProgressState(ProgressState.INTRODUCED);
        wordTranslationUserState.setProgressCounter(0);
        wordTranslationUserStateRepository.save(wordTranslationUserState);
    }

    public List<UserLanguageProgress> getUserProgress(long userId)
    {
        List<WordTranslationUserStateQueryItem> wordTranslationUserStates = wordTranslationUserStateRepository.findByUserId(userId);
        Map<String, List<WordTranslationUserStateQueryItem>> languageNameToProgressStates = wordTranslationUserStates.stream()
                .collect(Collectors.groupingBy(WordTranslationUserStateQueryItem::getLanguageName));

        return languageNameToProgressStates.keySet().stream()
                .map(languageName ->
                    {
                        List<WordTranslationUserStateQueryItem> userProgressStateQueryItems = languageNameToProgressStates.get(languageName);
                        List<WordTranslationUserStateDto> wordTranslationUserStateDtos = userProgressStateQueryItems.stream()
                                .map(userProgressStateQueryItem -> toDto(userProgressStateQueryItem))
                                .collect(Collectors.toList());
                        UserLanguageProgress userLanguageProgress = UserLanguageProgress.builder()
                                .wordTranslationUserStates(wordTranslationUserStateDtos)
                                .languageName(languageName)
                                .build();
                        return userLanguageProgress;
                    })
                .collect(Collectors.toList());
    }

    private WordTranslationUserStateDto toDto(WordTranslationUserStateQueryItem progressState)
    {
        return WordTranslationUserStateDto.builder()
                .wordValue(progressState.getWordValue())
                .wordTranslation(progressState.getWordTranslation())
                .progressState(progressState.getProgressState() + " " + progressState.getProgressCounter())
                .build();
    }

    public void clearUserProgress(long userId)
    {
        wordTranslationUserStateRepository.deleteByUserId(userId);
    }
}