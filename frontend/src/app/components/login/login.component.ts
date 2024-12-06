import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from '../../service/session.service';
import { AuthorizationService } from '../../service/authorization.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  model: any = { };
  sessionId: any = "";

  constructor(private router: Router, private sessionService: SessionService,
              private authorizationService: AuthorizationService,
              private titleSetter: Title) { }
              
  ngOnInit(): void {
    this.titleSetter.setTitle("PcForum - logging")
  }

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
