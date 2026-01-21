package com.denis.afoh.intelliShop;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "produits")
@Getter
@Setter
@NoArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private Double prix;

    @Column(nullable = false)
    private String imageUrl;

    public Produit(String nom, Double prix, String imageUrl) {
        this.nom =  nom;
        this.prix = prix;
        this.imageUrl = imageUrl;
    }
}
