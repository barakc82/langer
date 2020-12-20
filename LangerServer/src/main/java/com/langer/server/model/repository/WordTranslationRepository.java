package com.langer.server.model.repository;

import com.langer.server.api.admin.dto.WordWithTranslation;
import com.langer.server.model.entity.WordTranslationForStorage;
import com.langer.server.model.entity.impl.WordTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordTranslationRepository extends JpaRepository<WordTranslation, Long>
{
    List<WordTranslation> findAllByWordId(long id);

    WordTranslation findByWordIdAndTranslation(long id, String translation);

    @Query(
        "SELECT wordTranslation " +
            "FROM WordTranslation wordTranslation " +
            "GROUP BY wordId, translation " +
            "HAVING COUNT(wordTranslation) > 1"
    )
    List<WordTranslation> findAllWordTranslationsWithSameWord();

    @Query("SELECT " +
            "word.value AS wordValue, " +
            "word.languageId AS languageId, " +
            "wordTranslation.translation AS translation " +
            "FROM Word word " +
            "INNER JOIN WordTranslation wordTranslation ON word.id = wordTranslation.wordId AND wordTranslation.translation != 'Stub translation'"
    )
    List<WordTranslationForStorage> findAllTranslations();

    @Query("SELECT " +
            "word.id AS wordId, " +
            "word.value AS wordValue, " +
            "word.type AS wordType, " +
            "wordTranslation.id AS wordTranslationId, " +
            "wordTranslation.translation AS translation " +
            "FROM Word word " +
            "INNER JOIN WordTranslation wordTranslation ON word.id = wordTranslation.wordId AND wordTranslation.translation != 'Stub translation' " +
            "WHERE word.languageId = :languageId"
    )
    List<WordTranslationForStorage> findByLanguageId(long languageId);

    @Query("DELETE FROM WordTranslation " +
           "WHERE id IN " +
           "(SELECT wordTranslation.id " +
           "FROM WordTranslation wordTranslation " +
           "INNER JOIN Word word ON word.id = wordTranslation.wordId AND word.languageId = :languageId)"
    )
    void deleteByLanguageId(long languageId);
}