import { Injectable } from '@angular/core';

export type InteractionType = 'VIEW' | 'ADD_TO_CART' | 'REMOVE_FROM_CART' | 'CLICK';

export interface InteractionEvent {
  userId: number | null;       // si pas connecté, null (mais en pratique on loggé)
  produitId: number;
  type: InteractionType;
  ts: string;                  // ISO string
}

@Injectable({ providedIn: 'root' })
export class InteractionService {
  private readonly KEY = 'interactions_v1';

  logView(produitId: number, userId: number | null) {
    this.push({ userId, produitId, type: 'VIEW', ts: new Date().toISOString() });
  }

  logAddToCart(produitId: number, userId: number | null) {
    this.push({ userId, produitId, type: 'ADD_TO_CART', ts: new Date().toISOString() });
  }

  logRemoveFromCart(produitId: number, userId: number | null) {
    this.push({ userId, produitId, type: 'REMOVE_FROM_CART', ts: new Date().toISOString() });
  }

  logClick(produitId: number, userId: number | null) {
    this.push({ userId, produitId, type: 'CLICK', ts: new Date().toISOString() });
  }

  getAll(): InteractionEvent[] {
    return this.read();
  }

  getByUser(userId: number): InteractionEvent[] {
    return this.read().filter(e => e.userId === userId);
  }

  clear() {
    localStorage.removeItem(this.KEY);
  }

  // --- internal helpers ---
  private push(ev: InteractionEvent) {
    const all = this.read();
    all.push(ev);
    // Option: limiter pour éviter grossir
    const trimmed = all.slice(-2000);
    localStorage.setItem(this.KEY, JSON.stringify(trimmed));
  }

  private read(): InteractionEvent[] {
    const raw = localStorage.getItem(this.KEY);
    if (!raw) return [];
    try {
      const parsed = JSON.parse(raw);
      return Array.isArray(parsed) ? parsed : [];
    } catch {
      return [];
    }
  }
}
