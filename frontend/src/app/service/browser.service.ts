import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BrowserResultDTO } from '../model/BrowserResultDTO';

@Injectable({
  providedIn: 'root'
})
export class BrowserService {
  private readonly API: string = "http://localhost:8080/api/v1/browser";

  constructor(private httpClient: HttpClient) { }
  
  public find(searchedText: string): Observable<BrowserResultDTO> {
    return this.httpClient.get<BrowserResultDTO>(`${this.API}/find`, {
      params: {
        text: searchedText
      }
    });
  }
}
