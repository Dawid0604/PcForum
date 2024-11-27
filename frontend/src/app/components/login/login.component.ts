import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from '../../service/session.service';
import { AuthorizationService } from '../../service/authorization.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  model: any = { };
  sessionId: any = "";

  constructor(private router: Router, private sessionService: SessionService,
              private authorizationService: AuthorizationService) { }

  login() {
    this.authorizationService
        .login(this.model.username, this.model.password)
        .subscribe({
          next: _res => {
            this.sessionService.checkStatus();
            this.router.navigate([ "" ]);
          },
          error: _err => {
            alert("Authorization failed!");
            console.log(_err);
          }
        })
  }              
}
