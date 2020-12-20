package com.langer.server.model.entity.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@Table(
        indexes = {
                @Index(name = "USER_INDEX_0", columnList = "word_id"),
        })
public class WordTranslation
{
    @Id
    @GeneratedValue
    private long    id;

    @Column(name = "word_id", nullable = false)
    private long    wordId;

    @Column(length = 40, nullable = false)
    private String  translation;
}