import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ThreadsComponent } from './components/threads/threads.component';
import { SingleThreadComponent } from './components/single-thread/single-thread.component';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { CookieService } from 'ngx-cookie-service';
import { FormsModule } from '@angular/forms';
import { RequestInterceptor } from './interceptor/request.interceptor';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ThreadCreatorComponent } from './components/thread-creator/thread-creator.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { BrowserComponent } from './components/browser/browser.component';
import { UserProfileThreadsComponent } from './components/user-profile-threads/user-profile-threads.component';
import { UserProfilePostsComponent } from './components/user-profile-posts/user-profile-posts.component';
import { UserProfileFollowersComponent } from './components/user-profile-followers/user-profile-followers.component';
import { UserProfileVisitorsComponent } from './components/user-profile-visitors/user-profile-visitors.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NavbarComponent,
    ThreadsComponent,
    SingleThreadComponent,
    LoginComponent,
    RegisterComponent,
    ThreadCreatorComponent,
    UserProfileComponent,
    BrowserComponent,
    UserProfileThreadsComponent,
    UserProfilePostsComponent,
    UserProfileFollowersComponent,
    UserProfileVisitorsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    FontAwesomeModule
  ],
  providers: [
    CookieService,
    provideHttpClient(withInterceptorsFromDi()),
    { provide: HTTP_INTERCEPTORS, useClass: RequestInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
