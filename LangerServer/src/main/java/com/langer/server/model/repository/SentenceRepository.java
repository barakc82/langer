package com.langer.server.model.repository;

import com.langer.server.model.entity.impl.Sentence;
import com.langer.server.model.repository.queryitems.SentenceTranslation;
import com.langer.server.model.repository.queryitems.SentenceWithLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SentenceRepository extends JpaRepository<Sentence, Long>
{
    List<Sentence> findAllByParagraphId(long id);

    @Query("SELECT " +
            "language.name AS languageName, " +
            "resource.title AS resourceTitle, " +
            "episode.title AS episodeTitle, " +
            "paragraph.paragraphIndex AS paragraphIndex, " +
            "sentence.id AS sentenceId, " +
            "sentence.text AS sentenceText, " +
            "sentence.sentenceIndex AS sentenceIndex, " +
            "sentence.translation AS translation, " +
            "sentence.shouldBeIgnored AS shouldBeIgnored " +
            "FROM Sentence sentence " +
            "INNER JOIN Language language ON language.id = :languageId " +
            "INNER JOIN Resource resource ON resource.languageId = :languageId " +
            "INNER JOIN Episode episode ON episode.resourceId = resource.id " +
            "INNER JOIN Paragraph paragraph ON paragraph.episodeId = episode.id " +
            "WHERE sentence.paragraphId = paragraph.id"
    )
    List<SentenceWithLocation> findAllByLanguageId(@Param("languageId") long languageId);

    Sentence findByParagraphIdAndSentenceIndex(long id, int paragraphIndex);

    @Query("SELECT sentence FROM Sentence sentence " +
           "INNER JOIN Paragraph paragraph ON paragraph.episodeId = :episodeId " +
           "WHERE sentence.paragraphId = paragraph.id"
    )
    List<Sentence> findAllByEpisodeId(@Param("episodeId") long episodeId);

    @Query("SELECT " +
            "language.id AS languageId, " +
            "sentence.text AS text, " +
            "sentence.translation AS translation " +
            "FROM Language language " +
            "INNER JOIN Resource resource ON resource.languageId = language.id " +
            "INNER JOIN Episode episode ON episode.resourceId = resource.id " +
            "INNER JOIN Paragraph paragraph ON paragraph.episodeId = episode.id " +
            "INNER JOIN Sentence sentence ON sentence.paragraphId = paragraph.id " +
            "WHERE sentence.translation != NULL"
    )
    List<SentenceTranslation> findAllTranslations();

    @Query("SELECT sentence " +
            "FROM Sentence sentence " +
            "INNER JOIN Language language ON language.id = :languageId " +
            "INNER JOIN Resource resource ON resource.languageId = language.id " +
            "INNER JOIN Episode episode ON episode.resourceId = resource.id " +
            "INNER JOIN Paragraph paragraph ON paragraph.episodeId = episode.id " +
            "WHERE sentence.paragraphId = paragraph.id AND sentence.text = :text"
    )
    List<Sentence> findByTextAndLanguageId(@Param("text") String text, @Param("languageId") long languageId);

    List<Sentence> findByShouldBeIgnoredTrue();
}