import { Injectable } from '@angular/core';

import { AuthControllerService, LoginRequest, RegisterRequest, AuthResponse } from '../../api/generated';
import { Observable, tap , switchMap, map} from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})

export class Auth {
  private readonly baseUrl = 'http://localhost:8080/api/auth';
  private readonly TOKEN_KEY = 'token';
  private readonly USER_KEY = 'user';
  
  constructor( private readonly authApi: AuthControllerService, private http: HttpClient){}

  // register(payload: RegisterRequest): Observable<AuthResponse>{
  //   return this.authApi.register(payload).pipe(
  //     tap((res) => {
  //       console.log("REGISTER RES => ", res);
  //       this.saveAuth(res);
  //     })
  //   );
  // }


  register(payload: RegisterRequest): Observable<AuthResponse> {
    return this.http
      .post(`${this.baseUrl}/register`, payload, { responseType: 'text' })
      .pipe(
        map((txt) => JSON.parse(txt) as AuthResponse),
        tap((res) => this.saveAuth(res))
      );
  }

  // login(payload: LoginRequest): Observable<AuthResponse> {
  //   return this.authApi.login(payload).pipe(
  //     tap(res => {
  //       console.log('LOGIN RES => ', res);
  //       this.saveAuth(res);
  //     })
  //   );
  // }
  
  login(payload: LoginRequest): Observable<AuthResponse>{
    return this.http 
      .post(`${this.baseUrl}/login`, payload, {responseType: 'text' })
      .pipe(
        map((txt) => JSON.parse(txt) as AuthResponse),
        tap((res) => this.saveAuth(res))
        
      );
  }
 

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
  }
  // isLoggedIn(): boolean {
  //   return !!this.getToken();
  // }

  // isLoggedIn(): boolean {
  //   const t = this.getToken();
  //   return !!t && t !== 'null' && t.trim().length > 10;
  // }
  isLoggedIn(): boolean {
    const t = this.getToken();
    // On vérifie que le token existe, n'est pas une chaîne "null"/"undefined" 
    // et qu'il a une longueur crédible pour un JWT
    return !!t && t !== 'null' && t !== 'undefined' && t.trim().length > 20;
  }
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getUserSnapshot(): {userId: number, email: string, role: string} | null {
    const raw = localStorage.getItem(this.USER_KEY);
    return raw ? JSON.parse(raw) : null;
  }

  
  private blobToJson<T>(blob: Blob): Observable<T> {
    return new Observable(observer => {
      const reader = new FileReader();
      reader.onload = () => {
        observer.next(JSON.parse(reader.result as string));
        observer.complete();
      };
      reader.onerror = err => observer.error(err);
      reader.readAsText(blob);
    });
  }
  

  private saveAuth(res: AuthResponse): void {
    if(res?.token){
      localStorage.setItem(this.TOKEN_KEY, res.token);
    }else {
      localStorage.removeItem(this.TOKEN_KEY);
    }
    localStorage.setItem(
      this.USER_KEY,
      JSON.stringify({
        userId: res.userId,
        email: res.email,
        role: res.role,

      }

      )


    );
  }

 

}
