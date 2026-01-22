package com.denis.afoh.intelliShop.dto.commande;



import lombok.Data;

import java.util.List;

@Data
public class CreateCommandeRequest {
    private Long userId;
    private List<LigneCommandeRequest> items;

}
