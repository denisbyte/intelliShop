package com.denis.afoh.intelliShop.services.impl;

import com.denis.afoh.intelliShop.entity.Produit;

import com.denis.afoh.intelliShop.dto.commande.CreateCommandeRequest;
import com.denis.afoh.intelliShop.dto.commande.LigneCommandeRequest;
import com.denis.afoh.intelliShop.entity.Commande;
import com.denis.afoh.intelliShop.entity.LigneCommande;
import com.denis.afoh.intelliShop.entity.User;
import com.denis.afoh.intelliShop.repository.CommandeRepository;
import com.denis.afoh.intelliShop.repository.ProduitRepository;
import com.denis.afoh.intelliShop.repository.UserRepository;
import com.denis.afoh.intelliShop.services.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CommandeserviceImpl implements CommandeService {
    private final CommandeRepository commandeRepository;
    private final UserRepository userRepository;
    private final ProduitRepository produitRepository;
    @Override
    public Commande createCommande(CreateCommandeRequest request) {
        if(request == null){
            throw new RuntimeException("Request commande invalide");
        }
        if(request.getUserId() == null){
            throw new RuntimeException("userId est obligatoire");
        }
        if(request.getItems() == null || request.getItems().isEmpty()){
            throw new RuntimeException("La commande doit contenir au moins 1 item");
        }
        // Charger le User
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User introuvable id=" + request.getUserId()));
        Commande commande = new Commande();
        // Création de la commande
        commande.setUser(user);
        // Construction des lignes de commande
        List<LigneCommande> lignes = new ArrayList<>();
        for(LigneCommandeRequest item: request.getItems()){
            if(item.getProduitId() == null){
                throw new RuntimeException("produitId est obligatoire");
            }
            if(item.getQuantite() == null || item.getQuantite() <= 0){
                throw new RuntimeException("Quantité doit etre > 0");
            }
            // Charger le produit
            Produit produit = produitRepository.findById(item.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit Introuvable id =" + item.getProduitId()));

            // Création de la ligne de commande
            LigneCommande lc = new LigneCommande();
            lc.setCommande(commande);
            lc.setProduit(produit);
            lc.setQuantite(item.getQuantite());
            lc.setPrixUnitaire(produit.getPrix());
            lignes.add(lc);
        }
            commande.setLignesCommande(lignes);

            // Sauvegarde de la commande
            return commandeRepository.save(commande);

    }

    @Override
    public List<Commande> getCommandesByUser(Long userId) {
        if(userId == null){
            throw new RuntimeException("userId est obligatoire");
        }
        return commandeRepository.findByUserId(userId);
    }

    @Override
    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable id=" + id));
    }
}
