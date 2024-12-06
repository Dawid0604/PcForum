import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostReactionService {
  private readonly API = "http://localhost:8080/api/v1/post/reaction";

  constructor(private httpClient: HttpClient) { }

  public handleUpVote(postId: string): Observable<any> {
    return this.httpClient.patch<any>(`${this.API}/up_vote/${postId}`, { });
  }

  public handleDownVote(postId: string): Observable<any> {
    return this.httpClient.patch<any>(`${this.API}/down_vote/${postId}`, { });
  }
}
