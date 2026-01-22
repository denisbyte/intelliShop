package com.denis.afoh.intelliShop.dto.produit;

import lombok.Data;

@Data
public class ProduitRequest {
    private String nom;
    private Double prix;
    private String imageUrl;
}
