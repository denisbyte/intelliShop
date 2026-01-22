package com.denis.afoh.intelliShop.dto.produit;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProduitResponse {
    private Long id;
    private String nom;
    private Double prix;
    private String imageUrl;
}
