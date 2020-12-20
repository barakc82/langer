package com.langer.server.model.entity.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@Table(
        indexes = {
                @Index(name = "USER_INDEX_0", columnList = "user_id"),
                @Index(name = "USER_INDEX_1", columnList = "word_translation_id"),
        })
public class WordTranslationUserState
{
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "user_id")
    private long            userId;

    @Column(name = "word_translation_id")
    private long            wordTranslationId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "progress_state")
    private ProgressState   progressState;

    @Column(name = "progress_counter")
    private int             progressCounter;
}