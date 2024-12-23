import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { faCertificate, faCircle, faCommentMedical, faComments, faEnvelope, faEye, 
         faStar, faThumbsDown, faThumbsUp } from '@fortawesome/free-solid-svg-icons';
import { UserProfileDetailsDTO } from '../../model/UserProfileDetailsDTO';
import { UserProfileService } from '../../service/user-profile.service';
import { SessionService } from '../../service/session.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit {
  userProfile: UserProfileDetailsDTO = { } as UserProfileDetailsDTO;
  isUserLoggedIn: boolean = false;
  isUserProfileObserved: boolean = false;

  likeIcon = faThumbsUp;
  dislikeIcon = faThumbsDown;
  visitIcon = faEye;
  observationsIcon = faCertificate;
  observationIcon = faStar;
  postsIcon = faComments;
  threadsIcon = faCommentMedical;
  messageIcon = faEnvelope;
  onlineIcon = faCircle;

  constructor(private titleSetter: Title,
              private sessionService: SessionService,
              private userProfileService: UserProfileService,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.activatedRoute
        .paramMap
        .subscribe(_params => {
          const userId = _params.get("ref");
          const nickname = _params.get("nickname");

          if(userId) {                            
            this.sessionService
                .isUserLoggedIn
                .subscribe(_status => {
                  this.isUserLoggedIn = _status
                
                  if(_status) {
                    this.userProfileService
                        .handleUserProfileView(userId)
                        .subscribe({
                          next: _res => this.userProfileService
                                            .getDetailsInfo(userId)
                                            .subscribe({
                                              next: _res => this.userProfile = _res,
                                              error: _err => console.log(_err)
                                            }),
                          error: _err => console.log(_err)
                        })  

                  } else {
                    this.userProfileService
                        .getDetailsInfo(userId)
                        .subscribe({
                          next: _res => this.userProfile = _res,
                          error: _err => console.log(_err)
                        })
                  }
                });                
          }

          if(nickname) {
            this.titleSetter.setTitle("PcForum - " + nickname + " account")
          }
        })
  }

  observe() {
    this.userProfileService
        .handleUserProfileObservation(this.userProfile.encryptedId)
        .subscribe({
          next: _res => this.userProfileService
                            .getDetailsInfo(this.userProfile.encryptedId)
                            .subscribe({
                              next: _res => this.userProfile = _res,
                              error: _err => console.log(_err)
                            }),
          error: _err => console.log(_err)
        })
  }
}
