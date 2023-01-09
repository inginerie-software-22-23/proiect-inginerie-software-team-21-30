import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';
import { MentorGuard } from './guards/mentor.guard';
import { LoginComponent } from './modules/auth/components/login/login.component';
import { RegisterComponent } from './modules/auth/components/register/register.component';
import { ManageCoursesComponent } from './modules/courses/components/manage-courses/manage-courses.component';
import { ViewCoursesComponent } from './modules/courses/components/view-courses/view-courses.component';
import { HomeComponent } from './modules/home/components/home/home.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'manage-courses',
    component: ManageCoursesComponent,
    canActivate: [MentorGuard]
  },
  {
    path: 'view-courses',
    component: ViewCoursesComponent,
    canActivate: [AuthGuard]
  },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
