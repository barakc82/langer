package com.langer.server.model.repository;

import com.langer.server.model.entity.impl.ProgressState;
import com.langer.server.model.entity.impl.WordTranslationUserState;
import com.langer.server.model.repository.queryitems.WordTranslationUserStateQueryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface WordTranslationUserStateRepository extends JpaRepository<WordTranslationUserState, Long>
{
    WordTranslationUserState findByUserIdAndWordTranslationId(long userId, long wordTranslationId);

    List<WordTranslationUserState> findByUserIdAndProgressState(long userId, ProgressState introduced);

    @Query("SELECT " +
            "language.name AS languageName, " +
            "word.value AS wordValue, " +
            "wordTranslation.translation AS wordTranslation, " +
            "wordTranslationUserState.progressState AS progressState, " +
            "wordTranslationUserState.progressCounter AS progressCounter " +
            "FROM WordTranslationUserState wordTranslationUserState " +
            "INNER JOIN WordTranslation wordTranslation ON wordTranslation.id = wordTranslationUserState.wordTranslationId " +
            "INNER JOIN Word word ON word.id = wordTranslation.wordId " +
            "INNER JOIN Language language ON language.id = word.languageId " +
            "WHERE wordTranslationUserState.userId = :userId"
    )
    List<WordTranslationUserStateQueryItem> findByUserId(long userId);

    List<WordTranslationUserState> deleteByUserId(long userId);
}
