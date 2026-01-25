package com.denis.afoh.intelliShop.repository;

import com.denis.afoh.intelliShop.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
