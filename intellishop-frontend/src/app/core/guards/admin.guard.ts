import { CanActivateFn, Router } from "@angular/router";

import { inject } from "@angular/core";

import { Auth } from "../services/auth";

export const adminGuard: CanActivateFn = () => {
    const auth = inject(Auth);
    const router = inject(Router);

    const u = auth.getUserSnapshot();
    if(u?.role == 'ROLE_ADMIN') return true;

    router.navigate(['/produits']);
    return false;
}