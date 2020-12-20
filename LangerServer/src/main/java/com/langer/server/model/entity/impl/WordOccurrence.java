package com.langer.server.model.entity.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@Table(
        indexes = {
                @Index(name = "USER_INDEX_0", columnList = "sentence_id"),
                @Index(name = "USER_INDEX_1", columnList = "word_translation_id"),
        })
public class WordOccurrence
{
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "sentence_id")
    private long sentenceId;

    @Column(name = "word_translation_id")
    private long wordTranslationId;

    private int  position;

    @Enumerated(EnumType.STRING)
    @Column(name = "word_translation_type", length = 10)
    private WordTranslationType wordTranslationType;
}