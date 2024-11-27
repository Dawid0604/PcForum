import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private $loggedInObservable = new BehaviorSubject<boolean>(false);
  public isUserLoggedIn = this.$loggedInObservable.asObservable();

  constructor(private cookieService: CookieService) { 
    this.checkStatus()
  }

  public checkStatus() {
    if(this.cookieService.check("JSESSIONID")) {
      this.$loggedInObservable.next(true);

    } else {
      this.$loggedInObservable.next(false);
    }
  }

  public logout() {
    this.$loggedInObservable.next(false);
  }
}
