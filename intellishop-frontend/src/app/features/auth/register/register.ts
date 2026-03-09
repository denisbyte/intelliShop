import { Component } from '@angular/core';
import { Auth } from '../../../core/services/auth';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  nom = '';
  email = '';
  password = '';
  error = '';

  constructor(private auth: Auth, private router: Router ){}

  register(){
    this.auth
      .register({nom: this.nom, email: this.email, password: this.password})
      .subscribe({
          next: () => this.router.navigate(['/login']),
          error: () => (this.error = 'Erreur lors de l\'inscription'),
      });

  }
    
  

}
