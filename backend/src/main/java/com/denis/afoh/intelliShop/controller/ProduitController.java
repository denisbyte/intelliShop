package com.denis.afoh.intelliShop.controller;


import com.denis.afoh.intelliShop.entity.Produit;
import com.denis.afoh.intelliShop.dto.produit.ProduitRequest;
import com.denis.afoh.intelliShop.dto.produit.ProduitResponse;
import com.denis.afoh.intelliShop.services.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/produits")
public class ProduitController {
    private final ProduitService produitService;

    /**
     * Créer un produit.
     * (endpoint ADMIN only)
     */
    @PostMapping
    public ResponseEntity<ProduitResponse> createProduit(@RequestBody ProduitRequest request){
        Produit produit = produitService.createProduit(
                request.getNom(),
                request.getPrix(),
                request.getImageUrl()
        );

        // Mapping Entity --> Response DTO
        ProduitResponse response = toResponse(produit);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    // Récupérer un produit par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ProduitResponse> getProduitById(@PathVariable Long id){
        Produit produit = produitService.getProduitById(id);
        return ResponseEntity.ok(toResponse(produit));
    }
    // Lister tous les produits
    @GetMapping
    public ResponseEntity<List<ProduitResponse>> getAllProduits(){
        List<ProduitResponse> responses = produitService.getAllProduits()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProduitResponse> updateProduit(@PathVariable Long id, @RequestBody ProduitRequest request) {

        Produit updated = produitService.updateProduit(
                id,
                request.getNom(),
                request.getPrix(),
                request.getImageUrl()

        );
        return ResponseEntity.ok(toResponse((updated)));

    }

    /**
     * Supprimer un produit.
     * (endpoint ADMIN only)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id){
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }

    // Mapping Enity -> DTO
    private ProduitResponse toResponse(Produit produit) {
        return new ProduitResponse(
                produit.getId(),
                produit.getNom(),
                produit.getPrix(),
                produit.getImageUrl()
        );
    }


}
