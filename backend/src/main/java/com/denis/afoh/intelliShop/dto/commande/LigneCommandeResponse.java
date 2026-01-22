package com.denis.afoh.intelliShop.dto.commande;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LigneCommandeResponse {
    private Long ProduitId;
    private String nomProduit;
    private Integer quantite;
    private Double prixUnitaire;

}
