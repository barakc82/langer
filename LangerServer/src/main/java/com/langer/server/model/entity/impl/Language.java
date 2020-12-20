package com.langer.server.model.entity.impl;

import com.langer.server.api.data.enums.UserRole;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@Table(
        indexes = {
                @Index(name = "USER_INDEX_0", columnList = "name")
        })
public class Language
{
    @Id
    @GeneratedValue
    private long id;

    @Column(length = 50, nullable = false)
    private String name;
}
