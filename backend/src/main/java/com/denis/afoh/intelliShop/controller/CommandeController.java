package com.denis.afoh.intelliShop.controller;

import com.denis.afoh.intelliShop.dto.commande.CommandeResponse;
import com.denis.afoh.intelliShop.dto.commande.CreateCommandeRequest;
import com.denis.afoh.intelliShop.dto.commande.LigneCommandeResponse;
import com.denis.afoh.intelliShop.entity.Commande;
import com.denis.afoh.intelliShop.services.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {
    private final CommandeService commandeService;

    @PostMapping
    public ResponseEntity<CommandeResponse> createCommande(@RequestBody CreateCommandeRequest request){
        Commande commande = commandeService.createCommande(request);
        CommandeResponse response = mapToResponse(commande);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommandeResponse>> getCommandesByUser(@PathVariable Long userId){
        List<Commande> commandes = commandeService.getCommandesByUser(userId);
        List<CommandeResponse> responses = commandes.stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(responses);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponse> getCommandeById(@PathVariable Long id){
        Commande commande = commandeService.getCommandeById(id);
        CommandeResponse response = mapToResponse(commande);
        return ResponseEntity.ok(response);

    }

    private CommandeResponse mapToResponse(Commande commande){
        return new CommandeResponse(
                commande.getId(),
                commande.getDateCommande(),
                commande.getLignesCommande().stream()
                        .map(lc  -> new LigneCommandeResponse(
                                lc.getProduit().getId(),
                                lc.getProduit().getNom(),
                                lc.getQuantite(),
                                lc.getPrixUnitaire()
                        ))
                        .toList()
        );
    }

}
