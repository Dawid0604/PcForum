import { Component, OnInit } from '@angular/core';
import { SessionService } from '../../service/session.service';
import { Router } from '@angular/router';
import { AuthorizationService } from '../../service/authorization.service';
import { UserProfileService } from '../../service/user-profile.service';
import { UserProfileDTO } from '../../model/UserProfileDTO';
import { faBars, faPlus } from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  isUserLoggedIn: boolean = false;
  loggedUser: UserProfileDTO = { } as UserProfileDTO;

  accountSettingsIcon = faBars;
  newThreadIcon = faPlus;
  
  constructor(private sessionService: SessionService,
              private authorizationService: AuthorizationService,
              private userProfileService: UserProfileService,
              private router: Router) { }

  ngOnInit(): void {
    this.sessionService
        .isUserLoggedIn
        .subscribe(_status => {
          this.isUserLoggedIn = _status

          if(_status) {
            this.userProfileService
                .getBaseInfo()
                .subscribe({
                  next: _res => this.loggedUser = _res,
                  error: _err => console.log(_err)
                })
          }
        });
  }

  logout() {
    this.authorizationService
        .logout()
        .subscribe({
          next: _res => {
            this.sessionService.logout();
            this.router.navigate([ "" ]);
          },
          error: _err => {
            alert("Logout failed!");
            console.log(_err);
          }
        })
  }
}
