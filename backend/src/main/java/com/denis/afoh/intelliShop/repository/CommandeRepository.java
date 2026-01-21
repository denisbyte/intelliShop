package com.denis.afoh.intelliShop.repository;

import com.denis.afoh.intelliShop.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByUserId(Long userId);
}
