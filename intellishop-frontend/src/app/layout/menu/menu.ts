import { Component } from '@angular/core';
import { Auth } from '../../core/services/auth';
import { Router, RouterLinkActive, RouterModule } from '@angular/router';
import { CartService } from '../../core/services/cart.service';

@Component({
  selector: 'app-menu',
  imports: [RouterModule, RouterLinkActive],
  templateUrl: './menu.html',
  styleUrl: './menu.css',
})
export class Menu {

    constructor(public auth: Auth, private router: Router, private cart: CartService) {}
  
    logout() {
      this.cart.clear();
      this.auth.logout();
      // Ajout
      this.router.navigateByUrl('/login');
    }
  
    isAdmin(): boolean {
      const u = this.auth.getUserSnapshot();
      return u?.role === 'ROLE_ADMIN';
    }


    cartCount(): number {
      return this.cart.count();
    }
}
