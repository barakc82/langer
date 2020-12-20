package com.langer.server.model.repository;

import com.langer.server.model.entity.impl.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long>
{
    List<Resource> findAllByLanguageId(long languageId);

    boolean existsByTitle(String title);

    Resource findByTitleAndLanguageId(String resourceTitle, long id);
}
