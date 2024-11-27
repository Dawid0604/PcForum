import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationService {
  private readonly API: string = "http://localhost:8080/api/v1/access";

  constructor(private httpClient: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    return this.httpClient.post<any>(`${this.API}/login`, {
      username: username,
      password: password
    });
  }

  logout(): Observable<any> {
    return this.httpClient.post<any>(`${this.API}/logout`, { });
  }
}
