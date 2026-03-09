import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { blobToJson } from '../../../core/utils/blob-json';
import { ProduitControllerService, ProduitResponse } from '../../../api/generated';

import { CartService } from '../../../core/services/cart.service';
import { Auth } from '../../../core/services/auth';
import { InteractionService } from '../../../core/services/interaction.service';
import { RecommendationService } from '../../../core/services/recommendation.service';

@Component({
  selector: 'app-produits-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './produits-list.html',
  styleUrl: './produits-list.css',
})
export class ProduitsList implements OnInit {
  produits: ProduitResponse[] = [];
  recommendedIds: number[] = [];
  recommendedProduits: ProduitResponse[] = [];

  loading = true;
  error = '';

  constructor(
    private produitApi: ProduitControllerService,
    private cart: CartService,
    private interactions: InteractionService,
    private auth: Auth,
    private reco: RecommendationService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  isLoggedIn(): boolean {
    return this.auth.isLoggedIn();
  }

  private loadData() {
    this.loading = true;
    this.error = '';

    const produits$ = this.produitApi.getAllProduits().pipe(
      catchError(() => of([] as any))
    );

    // On ne charge les recos QUE si l'utilisateur est connecté
    const recoIds$ = this.isLoggedIn()
      ? this.reco.getRecommendedIdsStatic().pipe(catchError(() => of([] as number[])))
      : of([] as number[]);

    forkJoin({ produitsRes: produits$, ids: recoIds$ }).subscribe({
      next: async ({ produitsRes, ids }) => {
        const produits = produitsRes instanceof Blob
          ? await blobToJson<ProduitResponse[]>(produitsRes)
          : (produitsRes as ProduitResponse[]);

        this.produits = (produits ?? []).filter(p => this.isAzureUrl(p.imageUrl));
        this.recommendedIds = ids ?? [];
        
        // Appelle la fonction de reconstruction
        this.rebuildRecoList();

        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Erreur lors du chargement';
        this.loading = false;
      },
    });
  }

  /**
   * CORRECTION DU BUG : Liste statique
   * On ajoute un mélange (Shuffle) pour que les produits changent
   */
  private rebuildRecoList() {
    if (!this.isLoggedIn() || !this.recommendedIds.length) {
      this.recommendedProduits = [];
      return;
    }

    const set = new Set(this.recommendedIds.map(id => Number(id)));

    // 1. On récupère tous les produits qui correspondent aux IDs du JSON
    let candidates = this.produits.filter(p => p.id !== undefined && set.has(Number(p.id)));

    // 2. LOGIQUE DYNAMIQUE : On mélange l'ordre des candidats
    // Cela permet de ne pas toujours afficher les 8 mêmes produits en premier
    candidates = candidates.sort(() => Math.random() - 0.5);

    // 3. On prend les 8 premiers après le mélange
    this.recommendedProduits = candidates.slice(0, 8);
    
    console.log('Recos mises à jour (dynamique) :', this.recommendedProduits.length);
  }

  // --- Helpers ---
  isAzureUrl(url?: string | null): boolean {
    return !!url && url.includes('blob.core.windows.net');
  }

  imgSrc(p: ProduitResponse): string {
    const url = (p?.imageUrl ?? '').trim();
    return this.isAzureUrl(url) ? url : '';
  }

  hideImage(event: Event) {
    const img = event.target as HTMLImageElement;
    if (img) img.style.display = 'none';
  }

  addToCart(p: ProduitResponse) {
    if (!p.id) return;
    this.cart.add(p.id, 1);
    const u = this.auth.getUserSnapshot();
    this.interactions.logAddToCart(p.id, u?.userId ?? null);
  }

  onCardClick(p: ProduitResponse) {
    if (!p.id) return;
    const u = this.auth.getUserSnapshot();
    this.interactions.logClick(p.id, u?.userId ?? null);
  }
}