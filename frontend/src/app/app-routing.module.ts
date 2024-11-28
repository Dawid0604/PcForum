import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ThreadsComponent } from './components/threads/threads.component';
import { SingleThreadComponent } from './components/single-thread/single-thread.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ThreadCreatorComponent } from './components/thread-creator/thread-creator.component';
import { AuthorizationGuard } from './guard/authorization.guard';

const routes: Routes = [
  {
    path: "",
    component: HomeComponent
  },
  {
    path: "threads/:ref/:name",
    component: ThreadsComponent
  },
  {
    path: "thread/:ref/:name",
    component: SingleThreadComponent
  },
  {
    path: "thread/create",
    canActivate: [ AuthorizationGuard ],
    component: ThreadCreatorComponent
  },
  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "register",
    component: RegisterComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
