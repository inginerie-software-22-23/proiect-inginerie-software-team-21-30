import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog';
import { CourseCardComponent } from './course-card/course-card.component';
import { CourseDialogComponent } from './course-dialog/course-dialog.component';
import { HeaderComponent } from './header/header.component';



@NgModule({
  declarations: [
    HeaderComponent,
    CourseCardComponent,
    CourseDialogComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    CardModule,
    DialogModule,
    ButtonModule
  ],
  exports: [
    HeaderComponent,
    CourseCardComponent,
    CourseDialogComponent
  ]
})
export class SharedModule { }
