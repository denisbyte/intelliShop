package com.denis.afoh.intelliShop.dto.commande;


import com.denis.afoh.intelliShop.entity.LigneCommande;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CommandeResponse {
    private Long id;
    private LocalDateTime dateCommande;
    private List<LigneCommande> items;
}
