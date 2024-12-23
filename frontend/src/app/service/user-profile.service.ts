import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserProfileDTO } from '../model/UserProfileDTO';
import { UserProfileDetailsDTO } from '../model/UserProfileDetailsDTO';
import { ActivitySummaryDTO } from '../model/ActivitySummaryDTO';
import { UsersDTO } from '../model/UsersDTO';
import { UserProfileThreadsDTO } from '../model/UserProfileThreadsDTO';
import { UserProfilePostsDTO } from '../model/UserProfilePostsDTO';
import { UserProfileVisitorsDTO } from '../model/UserProfileVisitorsDTO';
import { UserProfileObservationsDTO } from '../model/UserProfileObservationsDTO';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {
  private readonly API = "http://localhost:8080/api/v1";

  constructor(private httpClient: HttpClient) { }

  public getBaseInfo(): Observable<UserProfileDTO> {
    return this.httpClient.get<UserProfileDTO>(`${this.API}/user/profile/base`);
  }

  public getDetailsInfo(userProfileId: string): Observable<UserProfileDetailsDTO> {
    return this.httpClient.get<UserProfileDetailsDTO>(`${this.API}/user/profile/${userProfileId}/details`);
  }

  public handleUserProfileView(userProfileId: string): Observable<any> {
    return this.httpClient.patch(`${this.API}/user/profile/${userProfileId}/handle/view`, { });
  }

  public handleUserProfileObservation(userProfileId: string): Observable<any> {
    return this.httpClient.patch(`${this.API}/user/profile/${userProfileId}/handle/observe`, { });
  }

  public getActivitySummary(activityMode: string): Observable<ActivitySummaryDTO> {
    return this.httpClient.get<ActivitySummaryDTO>(`${this.API}/user/activity/summary`, {
      params: {
        sort: activityMode ? activityMode : ""
      }
    });
  }

  public getNumberOfOnlineUsers(): Observable<UsersDTO> {
    return this.httpClient.get<UsersDTO>(`${this.API}/users/online`);
  }

  public findUserThreads(userProfileId: string): Observable<UserProfileThreadsDTO> {
    return this.httpClient.get<UserProfileThreadsDTO>(`${this.API}/user/profile/${userProfileId}/threads`);
  }

  public findUserPosts(userProfileId: string): Observable<UserProfilePostsDTO> {
    return this.httpClient.get<UserProfilePostsDTO>(`${this.API}/user/profile/${userProfileId}/posts`);
  }

  public findUserVisitors(userProfileId: string): Observable<UserProfileVisitorsDTO> {
    return this.httpClient.get<UserProfileVisitorsDTO>(`${this.API}/user/profile/${userProfileId}/visitors`);
  }

  public findUserObservations(userProfileId: string): Observable<UserProfileObservationsDTO> {
    return this.httpClient.get<UserProfileObservationsDTO>(`${this.API}/user/profile/${userProfileId}/observations`);
  }
}
