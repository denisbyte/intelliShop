package com.denis.afoh.intelliShop.repository;


import com.denis.afoh.intelliShop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Pour le login / JWT
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
