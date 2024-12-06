import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserProfileDTO } from '../model/UserProfileDTO';
import { UserProfileDetailsDTO } from '../model/UserProfileDetailsDTO';
import { ActivitySummaryDTO } from '../model/ActivitySummaryDTO';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {
  private readonly API = "http://localhost:8080/api/v1/user";

  constructor(private httpClient: HttpClient) { }

  public getBaseInfo(): Observable<UserProfileDTO> {
    return this.httpClient.get<UserProfileDTO>(`${this.API}/profile/base`);
  }

  public getDetailsInfo(userProfileId: string): Observable<UserProfileDetailsDTO> {
    return this.httpClient.get<UserProfileDetailsDTO>(`${this.API}/profile/${userProfileId}/details`);
  }

  public handleUserProfileView(userProfileId: string): Observable<any> {
    return this.httpClient.patch(`${this.API}/profile/${userProfileId}/handle/view`, { });
  }

  public getActivitySummary(activityMode: string): Observable<ActivitySummaryDTO> {
    return this.httpClient.get<ActivitySummaryDTO>(`${this.API}/activity/summary`, {
      params: {
        sort: activityMode ? activityMode : ""
      }
    });
  }
}
