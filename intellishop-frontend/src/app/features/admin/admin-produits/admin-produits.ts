
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

type Produit = {
  id: number;
  nom: string;
  prix: number;
  imageUrl?: string | null;
};

@Component({
  selector: 'app-admin-produits',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-produits.html',
  styleUrl: './admin-produits.css',
})
export class AdminProduits implements OnInit {
  private readonly baseUrl = 'http://localhost:8080/api/produits';

  produits: Produit[] = [];

  // UI state
  loading = false;
  error = '';
  showForm = false;
  isEdit = false;

  // form model
  form: { id?: number; nom: string; prix: number; imageUrl: string } = {
    nom: '',
    prix: 0,
    imageUrl: '',
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadProduits();
  }

  // ---------- LIST ----------
  loadProduits(): void {
    this.loading = true;
    this.error = '';

    this.http.get<Produit[]>(this.baseUrl).subscribe({
      next: (res) => {
        this.produits = res ?? [];
        this.loading = false;
      },
      error: () => {
        this.error = "Impossible de charger les produits";
        this.loading = false;
      },
    });
  }

  // ---------- FILTER / IMG ----------
  azureOnly(p: Produit): boolean {
    const url = (p?.imageUrl ?? '').trim();
    return url.includes('blob.core.windows.net');
  }

  imgSrc(p: Produit): string {
    const url = (p?.imageUrl ?? '').trim();
    return url ? url : 'assets/img/placeholder-product.png';
  }

  onImgError(event: Event) {
    const img = event.target as HTMLImageElement;
    img.src = 'assets/img/placeholder-product.png';
  }

  // ---------- CREATE ----------
  createProduit(): void {
    this.showForm = true;
    this.isEdit = false;
    this.form = { nom: '', prix: 0, imageUrl: '' };
  }

  // ---------- EDIT ----------
  startEdit(p: Produit): void {
    this.showForm = true;
    this.isEdit = true;
    this.form = {
      id: p.id,
      nom: p.nom,
      prix: p.prix,
      imageUrl: (p.imageUrl ?? '').toString(),
    };
  }

  cancel(): void {
    this.showForm = false;
    this.isEdit = false;
    this.form = { nom: '', prix: 0, imageUrl: '' };
  }

  save(): void {
    this.error = '';

    // validations rapides
    if (!this.form.nom.trim()) {
      this.error = "Le nom est obligatoire";
      return;
    }
    if (this.form.prix == null || Number.isNaN(this.form.prix) || this.form.prix <= 0) {
      this.error = "Le prix doit être > 0";
      return;
    }

    const payload = {
      nom: this.form.nom.trim(),
      prix: Number(this.form.prix),
      imageUrl: this.form.imageUrl?.trim() || null,
    };

    if (!this.isEdit) {
      // POST
      this.http.post<Produit>(this.baseUrl, payload).subscribe({
        next: () => {
          this.cancel();
          this.loadProduits();
        },
        error: () => (this.error = "Création impossible (vérifie ton rôle ADMIN + token)"),
      });
      return;
    }

    // PUT
    const id = this.form.id!;
    this.http.put<Produit>(`${this.baseUrl}/${id}`, payload).subscribe({
      next: () => {
        this.cancel();
        this.loadProduits();
      },
      error: () => (this.error = "Modification impossible (vérifie ton rôle ADMIN + token)"),
    });
  }

  // ---------- DELETE ----------
  deleteProduit(id: number): void {
    if (!confirm('Supprimer ce produit ?')) return;

    this.http.delete<void>(`${this.baseUrl}/${id}`).subscribe({
      next: () => this.loadProduits(),
      error: () => (this.error = "Suppression impossible (vérifie ton rôle ADMIN + token)"),
    });
  }
}
