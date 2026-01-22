package com.denis.afoh.intelliShop.dto.commande;


import lombok.Data;

@Data
public class LigneCommandeRequest {
    private Long produitId;
    private Integer quantite;
}
