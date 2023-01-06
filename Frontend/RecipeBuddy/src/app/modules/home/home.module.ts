import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './components/home/home.component';
import { CourseCardComponent } from './components/course-card/course-card.component';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { CourseDialogComponent } from './components/course-dialog/course-dialog.component';
import { DialogModule } from 'primeng/dialog';



@NgModule({
  declarations: [
    HomeComponent,
    CourseCardComponent,
    CourseDialogComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    CardModule,
    ButtonModule,
    DialogModule
  ],
  exports: [
    HomeComponent
  ]
})
export class HomeModule { }
