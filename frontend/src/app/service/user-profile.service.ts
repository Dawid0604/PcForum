import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserProfileDTO } from '../model/UserProfileDTO';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {
  private readonly API = "http://localhost:8080/api/v1/user";

  constructor(private httpClient: HttpClient) { }

  public getBaseInfo(): Observable<UserProfileDTO> {
    return this.httpClient.get<UserProfileDTO>(`${this.API}/profile/base`);
  }
}
