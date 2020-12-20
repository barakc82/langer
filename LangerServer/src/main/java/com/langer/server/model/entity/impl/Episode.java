package com.langer.server.model.entity.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@Table(
        indexes = {
                @Index(name = "USER_INDEX_0", columnList = "resource_id"),
        })
public class Episode
{
    @Id
    @GeneratedValue
    private long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String text;

    @Column(name = "resource_id")
    private long resourceId;

    private int  episodeIndex;
}