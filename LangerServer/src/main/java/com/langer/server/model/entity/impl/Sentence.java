package com.langer.server.model.entity.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@Table(
        indexes = {
                @Index(name = "USER_INDEX_0", columnList = "paragraph_id"),
        })
public class Sentence
{
    @Id
    @GeneratedValue
    private long    id;

    @Column(name = "paragraph_id")
    private long    paragraphId;

    @Column(nullable = false)
    private int     sentenceIndex;

    @Column(length = 1000, nullable = false)
    private String  text;

    private String  translation;
    private boolean shouldBeIgnored;
}