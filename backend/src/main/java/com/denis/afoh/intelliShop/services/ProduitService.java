package com.denis.afoh.intelliShop.services;

import com.denis.afoh.intelliShop.entity.Produit;

import java.util.List;

public interface ProduitService {
    Produit createProduit(String nom, Double prix, String imageUrl);

    Produit updateProduit(Long id, String nom, Double prix, String imageUrl);


    Produit  getProduitById(Long id);

    List<Produit> getAllProduits();

    void deleteProduit(Long id);
}
