package com.langer.server.model.repository;

import com.langer.server.model.entity.impl.Word;
import com.langer.server.model.entity.impl.WordOccurrence;
import com.langer.server.model.entity.impl.WordTranslationType;
import com.langer.server.model.repository.queryitems.WordOccurrenceWithTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface WordOccurrenceRepository extends JpaRepository<WordOccurrence, Long>
{
    @Query("SELECT " +
            "word.id AS wordId, " +
            "word.value AS wordValue, " +
            "word.type AS wordType, " +
            "wordTranslation.id AS wordTranslationId, " +
            "wordTranslation.translation AS wordTranslation, " +
            "wordOccurrence.id AS wordOccurrenceId, " +
            "wordOccurrence.position AS wordPosition " +
            "FROM Word word " +
            "INNER JOIN Sentence sentence ON sentence.id = :sentenceId " +
            "INNER JOIN WordOccurrence wordOccurrence ON wordOccurrence.sentenceId = sentence.id " +
            "INNER JOIN WordTranslation wordTranslation ON wordTranslation.id = wordOccurrence.wordTranslationId " +
            "WHERE word.id = wordTranslation.wordId"
    )
    List<WordOccurrenceWithTranslation> findAllBySentenceId(@Param("sentenceId") long sentenceId);

    @Query("SELECT " +
            "word.id AS wordId, " +
            "word.value AS wordValue, " +
            "word.type AS wordType, " +
            "wordTranslation.id AS wordTranslationId, " +
            "wordTranslation.translation AS wordTranslation, " +
            "wordOccurrence.id AS wordOccurrenceId, " +
            "wordOccurrence.position AS wordPosition, " +
            "wordOccurrence.sentenceId AS sentenceId " +
            "FROM Word word " +
            "INNER JOIN Language language ON language.id = :languageId " +
            "INNER JOIN Resource resource ON resource.languageId = :languageId " +
            "INNER JOIN Episode episode ON episode.resourceId = resource.id " +
            "INNER JOIN Paragraph paragraph ON paragraph.episodeId = episode.id " +
            "INNER JOIN Sentence sentence ON sentence.paragraphId = paragraph.id " +
            "INNER JOIN WordOccurrence wordOccurrence ON wordOccurrence.sentenceId = sentence.id " +
            "INNER JOIN WordTranslation wordTranslation ON wordTranslation.id = wordOccurrence.wordTranslationId " +
            "WHERE word.id = wordTranslation.wordId"
    )
    List<WordOccurrenceWithTranslation> findAllByLanguageId(long languageId);

    @Modifying
    @Query("DELETE FROM WordOccurrence wordOccurrence " +
           "WHERE wordOccurrence.wordTranslationType = com.langer.server.model.entity.impl.WordTranslationType.AUTOMATIC")
    void deleteAllAutomaticWordOccurrences();

    @Modifying
    void deleteByWordTranslationType(WordTranslationType wordTranslationType);

    @Query("DELETE FROM WordOccurrence " +
            "WHERE id IN " +
            "(SELECT wordOccurrence.id " +
            "FROM WordOccurrence wordOccurrence " +
            "INNER JOIN WordTranslation wordTranslation ON wordTranslation.id = wordOccurrence.wordTranslationId " +
            "INNER JOIN Word word ON wordTranslation.id = wordOccurrence.wordTranslationId AND word.languageId = :languageId)"
    )
    void deleteByLanguageId(long languageId);
}