import { Component } from '@angular/core';
import { UserProfileService } from '../../service/user-profile.service';
import { ActivatedRoute } from '@angular/router';
import { faComments } from '@fortawesome/free-solid-svg-icons';
import { UserProfilePostsDTO } from '../../model/UserProfilePostsDTO';

@Component({
  selector: 'app-user-profile-posts',
  templateUrl: './user-profile-posts.component.html',
  styleUrl: './user-profile-posts.component.css'
})
export class UserProfilePostsComponent {
  userProfile: UserProfilePostsDTO = { } as UserProfilePostsDTO;
  postsIcon = faComments;

  constructor(private userProfileService: UserProfileService,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.activatedRoute
        .paramMap
        .subscribe(_params => {
          const userProfileId = _params.get("ref");

          if(userProfileId) {
            this.userProfileService
                .findUserPosts(userProfileId)
                .subscribe({
                  next: _res => this.userProfile = _res,
                  error: _err => console.log(_err)
                })
          }
        })
  }
}
