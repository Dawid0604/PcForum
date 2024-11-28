import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GroupedThreadCategoryDTO } from '../model/GroupedThreadCategoryDTO';
import { CreatorThreadSubCategoryDTO } from '../model/CreatorThreadCategoryDTO';

@Injectable({
  providedIn: 'root'
})
export class ThreadCategoryService {
  private readonly API = "http://localhost:8080/api/v1/threads";

  constructor(private httpClient: HttpClient) { }

  public findAll(): Observable<GroupedThreadCategoryDTO[]> {
    return this.httpClient.get<GroupedThreadCategoryDTO[]>(`${this.API}/categories`);
  }

  public findSubCategories(encryptedId: string): Observable<GroupedThreadCategoryDTO> {
    return this.httpClient.get<GroupedThreadCategoryDTO>(`${this.API}/categories/${encryptedId}/sub`);
  }

  public findAllCreator(): Observable<GroupedThreadCategoryDTO[]> {
    return this.httpClient.get<GroupedThreadCategoryDTO[]>(`${this.API}/creator/categories`);
  }

  public findCreatorSubCategories(encryptedId: string): Observable<CreatorThreadSubCategoryDTO[]> {
    return this.httpClient.get<CreatorThreadSubCategoryDTO[]>(`${this.API}/creator/categories/${encryptedId}/sub`);
  }
}
