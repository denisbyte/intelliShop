import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, of } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RecommendationService {
  constructor(private http: HttpClient) {}

  // Option 1
  // getRecommendedIdsStatic(): Observable<number[]> {
  //   return this.http.get<number[]>('recommendations.json');
  // }
  getRecommendedIdsStatic(): Observable<number[]> {
    // On récupère le tableau d'objets et on ne garde que les valeurs de la clé 'id'
    return this.http.get<any[]>('recommendations.json').pipe(
      map(data => data.map(item => item.id))
    );
  }

 
}
