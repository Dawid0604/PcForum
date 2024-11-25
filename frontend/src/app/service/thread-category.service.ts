import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GroupedThreadCategoryDTO } from '../model/GroupedThreadCategoryDTO';

@Injectable({
  providedIn: 'root'
})
export class ThreadCategoryService {
  private readonly API = "http://localhost:8080/api/v1/threads/categories";

  constructor(private httpClient: HttpClient) { }

  public findAll(): Observable<GroupedThreadCategoryDTO[]> {
    return this.httpClient.get<GroupedThreadCategoryDTO[]>(`${this.API}`);
  }

  public findSubCategories(encryptedId: string): Observable<GroupedThreadCategoryDTO> {
    return this.httpClient.get<GroupedThreadCategoryDTO>(`${this.API}/${encryptedId}/sub`);
  }
}
