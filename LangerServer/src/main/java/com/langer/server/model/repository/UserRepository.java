package com.langer.server.model.repository;

import com.langer.server.model.entity.impl.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>
{
    User findFirstByUsername(String username);
    boolean existsByUsername(String username);
}