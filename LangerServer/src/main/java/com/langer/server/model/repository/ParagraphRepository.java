package com.langer.server.model.repository;

import com.langer.server.model.entity.impl.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ParagraphRepository extends JpaRepository<Paragraph, Long>
{
    Paragraph findByEpisodeIdAndParagraphIndex(long episodeTitle, int paragraphIndex);

    void deleteAllByEpisodeId(long id);
}