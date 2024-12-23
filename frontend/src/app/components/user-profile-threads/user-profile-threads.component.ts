import { Component, OnInit } from '@angular/core';
import { UserProfileService } from '../../service/user-profile.service';
import { UserProfileThreadsDTO } from '../../model/UserProfileThreadsDTO';
import { faCommentMedical } from '@fortawesome/free-solid-svg-icons';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user-profile-threads',
  templateUrl: './user-profile-threads.component.html',
  styleUrl: './user-profile-threads.component.css'
})
export class UserProfileThreadsComponent implements OnInit {
  userProfile: UserProfileThreadsDTO = { } as UserProfileThreadsDTO;
  threadsIcon = faCommentMedical;

  constructor(private userProfileService: UserProfileService,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.activatedRoute
        .paramMap
        .subscribe(_params => {
          const userProfileId = _params.get("ref");

          if(userProfileId) {
            this.userProfileService
                .findUserThreads(userProfileId)
                .subscribe({
                  next: _res => this.userProfile = _res,
                  error: _err => console.log(_err)
                })
          }
        })
  }
}
