import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ThreadsComponent } from './components/threads/threads.component';
import { SingleThreadComponent } from './components/single-thread/single-thread.component';

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
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
