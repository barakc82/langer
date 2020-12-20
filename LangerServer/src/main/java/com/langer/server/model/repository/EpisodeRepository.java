package com.langer.server.model.repository;

import com.langer.server.model.entity.impl.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EpisodeRepository extends JpaRepository<Episode, Long>
{
    List<Episode> findAllByResourceId(long id);

    boolean existsByTitleAndResourceId(String title, long resourceId);

    Episode findByTitleAndResourceId(String episodeTitle, long id);

    @Query("SELECT episode " +
            "FROM Episode episode " +
            "INNER JOIN Resource resource ON resource.languageId = :languageId " +
            "WHERE episode.resourceId = resource.id"
    )
    List<Episode> findAllByLanguageId(@Param("languageId") long languageId);
}