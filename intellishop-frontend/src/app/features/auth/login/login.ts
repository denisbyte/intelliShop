import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Auth } from '../../../core/services/auth';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email = '';
  password = '';
  error = ''
  
  constructor( private router: Router, private auth: Auth){}

  login(){
    this.auth.login({email: this.email, password: this.password}).subscribe({
      // next: () => this.router.navigate(['/produits']),
      next: () => this.router.navigateByUrl('/produits'),
      error: () => (this.error = 'Email ou mot de passe incorrect'),
    });
  }

}
