package com.denis.afoh.intelliShop.services.impl;

import com.denis.afoh.intelliShop.entity.Produit;
import com.denis.afoh.intelliShop.repository.ProduitRepository;
import com.denis.afoh.intelliShop.services.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProduitServiceImpl implements ProduitService {
    private final ProduitRepository produitRepository;
    @Override
    public Produit createProduit(String nom, Double prix, String imageUrl) {
        if(nom == null || nom.trim().isEmpty()){
            throw new RuntimeException("Le nom du produit est obligatoire");
        }
        if(prix == null || prix <= 0){
            throw new RuntimeException("le prix doit etre > 0");
        }
        if(imageUrl == null || imageUrl.trim().isEmpty()){
            throw new RuntimeException(" L'imageUrl est obligatoire");
        }
        // Création et sauvegrade
        Produit produit = new Produit();
        produit.setNom(nom);
        produit.setPrix(prix);
        produit.setImageUrl(imageUrl);
        return produitRepository.save(produit);
    }

    @Override
    public Produit updateProduit(Long id, String nom, Double prix, String imageUrl) {
        Produit existing = getProduitById(id);
        if(nom == null || nom.trim().isEmpty()){
            throw new RuntimeException(" Le nom du produit est obligatoire");
        }
        if(prix == null || prix <= 0){
            throw new RuntimeException("Le prix doit etre > 0");
        }
        if(imageUrl == null || imageUrl.trim().isEmpty()){
            throw new RuntimeException(" L'imageUrl est obligatoire");
        }
        existing.setNom(nom);
        existing.setPrix(prix);
        existing.setImageUrl(imageUrl);

        // Sauvegarde en base
        return produitRepository.save(existing);
    }

    @Override
    public Produit getProduitById(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,"Produit introuvable, id =" + id));
    }

    @Override
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    @Override
    public void deleteProduit(Long id) {
        if(!produitRepository.existsById(id)){
            throw new RuntimeException("Suppression impossible, produit introuvable id=" + id);
        }
        produitRepository.deleteById(id);

    }
}
