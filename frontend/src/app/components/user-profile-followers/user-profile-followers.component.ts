import { Component } from '@angular/core';
import { UserProfileObservationsDTO } from '../../model/UserProfileObservationsDTO';
import { faCalendarDays, faCircleUser } from '@fortawesome/free-solid-svg-icons';
import { UserProfileService } from '../../service/user-profile.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user-profile-followers',
  templateUrl: './user-profile-followers.component.html',
  styleUrl: './user-profile-followers.component.css'
})
export class UserProfileFollowersComponent {
  userProfile: UserProfileObservationsDTO = { } as UserProfileObservationsDTO;
  userProfileIcon = faCircleUser;
  dateIcon = faCalendarDays;

  constructor(private userProfileService: UserProfileService,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.activatedRoute
        .paramMap
        .subscribe(_params => {
          const userProfileId = _params.get("ref");

          if(userProfileId) {
            this.userProfileService
                .findUserObservations(userProfileId)
                .subscribe({
                  next: _res => this.userProfile = _res,
                  error: _err => console.log(_err)
                })
          }
        })
  }
}
