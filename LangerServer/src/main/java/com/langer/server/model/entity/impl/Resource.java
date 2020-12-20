package com.langer.server.model.entity.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@Table(
        indexes = {
                @Index(name = "USER_INDEX_0", columnList = "title"),
                @Index(name = "USER_INDEX_1", columnList = "language_id")
        })
public class Resource
{
    @Id
    @GeneratedValue
    private long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(name = "language_id")
    private long languageId;
}