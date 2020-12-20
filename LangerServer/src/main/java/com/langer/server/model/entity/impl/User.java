package com.langer.server.model.entity.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.langer.server.api.data.enums.UserRole;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@Table(
        indexes = {
                @Index(name = "USER_INDEX_0", columnList = "username")
        })
public class User
{
    @Id
    @GeneratedValue
    private long id;

    @Column(length = 50, nullable = false)
    private String username;

    @Column(nullable = false)
    private long hash;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserRole role;
}
