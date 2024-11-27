import { Component, OnInit } from '@angular/core';
import { SessionService } from '../../service/session.service';
import { Router } from '@angular/router';
import { AuthorizationService } from '../../service/authorization.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  isUserLoggedIn: boolean = false;
  
  constructor(private sessionService: SessionService,
              private authorizationService: AuthorizationService,
              private router: Router) { }

  ngOnInit(): void {
    this.sessionService
        .isUserLoggedIn
        .subscribe(_status => this.isUserLoggedIn = _status);
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
