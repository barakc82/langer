package com.langer.server.model.repository;

import com.langer.server.api.admin.dto.WordType;
import com.langer.server.model.entity.impl.Word;
import com.langer.server.model.entity.impl.WordTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface WordRepository extends JpaRepository<Word, Long>
{
    Word findByValueAndLanguageId(String wordValue, long id);

    void deleteByLanguageId(long languageId);

    @Query(
        "SELECT word " +
        "FROM Word word " +
        "GROUP BY value, languageId " +
        "HAVING COUNT(word) > 1"
    )
    List<Word> findAllWordsWithSameValueAndLanguageId();

    List<Word> findByTypeNotLike(WordType type);
}