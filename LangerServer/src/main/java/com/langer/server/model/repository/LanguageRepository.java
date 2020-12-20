package com.langer.server.model.repository;

import com.langer.server.model.entity.impl.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LanguageRepository extends JpaRepository<Language, Long>
{
    boolean existsByName(String name);

    Language findByName(String name);

    @Query("SELECT language FROM Language language " +
            "INNER JOIN Resource resource ON resource.languageId = language.id " +
            "INNER JOIN Episode episode ON episode.resourceId = resource.id " +
            "WHERE episode.id = :episodeId"
    )
    Language findByEpisodeId(@Param("episodeId") long episodeId);

    @Query("SELECT language FROM Language language " +
            "INNER JOIN Word word ON word.languageId = language.id " +
            "INNER JOIN WordTranslation wordTranslation ON wordTranslation.wordId = word.id " +
            "WHERE wordTranslation.id = :wordTranslationId"
    )
    Language findByWordTranslationId(@Param("wordTranslationId") long wordTranslationId);
}