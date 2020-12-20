package com.langer.server.model.entity.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@Table(
        indexes = {
                @Index(name = "USER_INDEX_0", columnList = "episode_id"),
        })
public class Paragraph
{
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "episode_id")
    private long episodeId;

    private int  paragraphIndex;
}