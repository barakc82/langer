package com.langer.server.model.entity.impl;

import com.langer.server.api.admin.dto.WordType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@Table(
        indexes = {
                @Index(name = "USER_INDEX_0", columnList = "value"),
                @Index(name = "USER_INDEX_1", columnList = "language_id"),
        })
public class Word
{
    @Id
    @GeneratedValue
    private long        id;

    private String      value;

    @Column(name = "language_id")
    private long        languageId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private WordType    type;
}