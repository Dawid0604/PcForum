import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostDTO } from '../model/PostDTO';
import { Pageable } from '../model/Pageable';
import { Observable } from 'rxjs';
import { NewestPostDTO } from '../model/NewestPostDTO';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private readonly API = "http://localhost:8080/api/v1/post";

  constructor(private httpClient: HttpClient) { }

  public findAllByThread(threadId: string): Observable<Pageable<PostDTO>> {
    return this.httpClient.get<Pageable<PostDTO>>(`${this.API}/${threadId}`);
  }

  public findNewestPosts(): Observable<NewestPostDTO[]> {
    return this.httpClient.get<NewestPostDTO[]>(`${this.API}/newest`);
  }

  public create(payload: any): Observable<any> {
    return this.httpClient.post<any>(`${this.API}/create`, payload);
  }

  public delete(postId: string): Observable<any> {
    return this.httpClient.delete<any>(`${this.API}/${postId}`);
  }
}
