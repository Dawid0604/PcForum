import { Component, OnInit } from '@angular/core';
import { SessionService } from '../../service/session.service';
import { Router } from '@angular/router';
import { AuthorizationService } from '../../service/authorization.service';
import { UserProfileService } from '../../service/user-profile.service';
import { UserProfileDTO } from '../../model/UserProfileDTO';
import { faBars, faBell, faCircle, faPlus, faUserGroup } from '@fortawesome/free-solid-svg-icons'
import { interval, Subscription, switchMap } from 'rxjs';
import { UsersDTO } from '../../model/UsersDTO';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  isUserLoggedIn: boolean = false;
  loggedUser: UserProfileDTO = { } as UserProfileDTO;
  userNotifications: any[] = [];
  users: UsersDTO = { } as UsersDTO;
  
  onlineIcon = faCircle;
  accountSettingsIcon = faBars;
  newThreadIcon = faPlus;
  notificationIcon = faBell;
  usersIcon = faUserGroup;
  form: any = { };
  
  private numberOfOnlineUsersSubscription!: Subscription;

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

    this.userProfileService
        .getNumberOfOnlineUsers()
        .subscribe({
          next: _res => this.users = _res,
          error: _err => console.log(_err)
        })
  
    this.numberOfOnlineUsersSubscription = interval(30_000).pipe(switchMap(() => this.userProfileService.getNumberOfOnlineUsers()))
                                                           .subscribe({
                                                             next: _res => this.users = _res,
                                                             error: _err => console.log(_err)
                                                           });                     
  }

  ngOnDestroy(): void {
    if(this.numberOfOnlineUsersSubscription) {
      this.numberOfOnlineUsersSubscription.unsubscribe();
    }
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

  browse() {
    if(this.form.text && (this.form.text as string).length >= 3) {
      this.router.navigate([ "/browser", this.form.text ]);
      this.form.text = "";
    }
  }
}
