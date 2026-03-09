import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { ProduitsList } from './features/produits/produits-list/produits-list';
import { MainLayout } from './layout/main-layout/main-layout';
import { AdminProduits } from './features/admin/admin-produits/admin-produits';
import { adminGuard } from './core/guards/admin.guard';



export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'register', component: Register },
  
    {
      path: '',
      component: MainLayout,
      children: [
        {
          path: 'produits',
          loadComponent: () =>
            import('./features/produits/produits-list/produits-list').then(m => m.ProduitsList),
        },
        {
          path: 'admin/produits',
          loadComponent: () =>
            import('./features/admin/admin-produits/admin-produits')
              .then(m => m.AdminProduits), canActivate:[adminGuard]
        },
  
        { path: '', redirectTo: 'produits', pathMatch: 'full' },
      ],
    },
  
    { path: '**', redirectTo: 'login' },
  ];