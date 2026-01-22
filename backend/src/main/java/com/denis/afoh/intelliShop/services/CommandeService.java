package com.denis.afoh.intelliShop.services;

import com.denis.afoh.intelliShop.dto.commande.CreateCommandeRequest;
import com.denis.afoh.intelliShop.entity.Commande;

import java.util.List;

public interface CommandeService {
    /**
     * Crée une commande :
     * - vérifie user
     * - vérifie produits
     * - crée Commande + lignesCommande
     * - calcule prixUnitaire depuis Produit
     * - sauvegarde en base
     */

    Commande createCommande(CreateCommandeRequest request);

    List<Commande> getCommandesByUser(Long userId);

    Commande getCommandeById(Long id);

}
