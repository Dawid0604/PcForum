import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostDTO } from '../model/PostDTO';
import { Pageable } from '../model/Pageable';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private readonly API = "http://localhost:8080/api/v1/post";

  constructor(private httpClient: HttpClient) { }

  public findAllByThread(threadId: string): Observable<Pageable<PostDTO>> {
    return this.httpClient.get<Pageable<PostDTO>>(`${this.API}/${threadId}`);
  }

}
