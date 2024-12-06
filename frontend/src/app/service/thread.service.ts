import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ThreadDTO } from '../model/ThreadDTO';
import { Observable } from 'rxjs';
import { Pageable } from '../model/Pageable';
import { ThreadDetailsDTO } from '../model/ThreadDetailsDTO';
import { MostPopularThreadDTO } from '../model/MostPopularThreadDTO';

@Injectable({
  providedIn: 'root'
})
export class ThreadService {
  private readonly API = "http://localhost:8080/api/v1";

  constructor(private httpClient: HttpClient) { }

  public findAllByCategory(categoryId: string): Observable<Pageable<ThreadDTO>> {
    return this.httpClient.get<Pageable<ThreadDTO>>(`${this.API}/threads/${categoryId}`);
  }

  public findDetails(threadId: string): Observable<ThreadDetailsDTO> {
    return this.httpClient.get<ThreadDetailsDTO>(`${this.API}/thread/${threadId}`);
  }

  public handleThreadView(threadId: string): Observable<any> {
    return this.httpClient.patch(`${this.API}/thread/${threadId}/handle/view`, { });
  }

  public findMostPopularThreads(): Observable<MostPopularThreadDTO[]> {
    return this.httpClient.get<MostPopularThreadDTO[]>(`${this.API}/threads/popular`);
  }

  public create(payload: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}/thread/create`, payload);
  }

  public close(threadId: string): Observable<any> {
    return this.httpClient.patch<any>(`${this.API}/thread/${threadId}/close`, { });
  }

  public delete(threadId: string): Observable<any> {
    return this.httpClient.delete<any>(`${this.API}/thread/${threadId}`);
  }
}
