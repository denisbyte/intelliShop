// import { Injectable } from '@angular/core';

// type CartItem = { produitId: number; qty: number };

// @Injectable({ providedIn: 'root' })
// export class CartService {
//   private readonly KEY = 'cart';

//   getItems(): CartItem[] {
//     const raw = localStorage.getItem(this.KEY);
//     return raw ? JSON.parse(raw) : [];
//   }

//   add(produitId: number, qty = 1) {
//     const items = this.getItems();
//     const found = items.find(i => i.produitId === produitId);
//     if (found) found.qty += qty;
//     else items.push({ produitId, qty });
//     localStorage.setItem(this.KEY, JSON.stringify(items));
//   }

//   clear() {
//     localStorage.removeItem(this.KEY);
//   }

//   count(): number {
//     return this.getItems().reduce((acc, i) => acc + i.qty, 0);
//   }
// }

import { Injectable } from '@angular/core';
import { Auth } from './auth';

type CartMap = Record<string, number>; // productId -> qty

@Injectable({ providedIn: 'root' })
export class CartService {
  constructor(private auth: Auth) {}

  private key(): string {
    const u = this.auth.getUserSnapshot();
    // user connecté -> panier séparé
    if (u?.userId) return `cart_v1_user_${u.userId}`;
    // invité (si tu autorises) -> panier séparé aussi
    return 'cart_v1_guest';
  }

  private read(): CartMap {
    const raw = localStorage.getItem(this.key());
    if (!raw) return {};
    try {
      const parsed = JSON.parse(raw);
      return parsed && typeof parsed === 'object' ? parsed : {};
    } catch {
      return {};
    }
  }

  private write(cart: CartMap) {
    localStorage.setItem(this.key(), JSON.stringify(cart));
  }

  add(productId: number, qty = 1) {
    const cart = this.read();
    const k = String(productId);
    cart[k] = (cart[k] ?? 0) + qty;
    this.write(cart);
  }

  remove(productId: number, qty = 1) {
    const cart = this.read();
    const k = String(productId);
    cart[k] = (cart[k] ?? 0) - qty;
    if (cart[k] <= 0) delete cart[k];
    this.write(cart);
  }

  count(): number {
    const cart = this.read();
    return Object.values(cart).reduce((sum, q) => sum + (q ?? 0), 0);
  }

  clear() {
    localStorage.removeItem(this.key());
  }
}

