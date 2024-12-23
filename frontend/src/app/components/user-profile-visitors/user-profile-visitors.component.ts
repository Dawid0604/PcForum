import { Component } from '@angular/core';
import { faCalendarDays, faCircleUser } from '@fortawesome/free-solid-svg-icons';
import { UserProfileService } from '../../service/user-profile.service';
import { ActivatedRoute } from '@angular/router';
import { UserProfileVisitorsDTO } from '../../model/UserProfileVisitorsDTO';

@Component({
  selector: 'app-user-profile-visitors',
  templateUrl: './user-profile-visitors.component.html',
  styleUrl: './user-profile-visitors.component.css'
})
export class UserProfileVisitorsComponent {  
  userProfile: UserProfileVisitorsDTO = { } as UserProfileVisitorsDTO;

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
                .findUserVisitors(userProfileId)
                .subscribe({
                  next: _res => this.userProfile = _res,
                  error: _err => console.log(_err)
                })
          }
        })
  }
}
