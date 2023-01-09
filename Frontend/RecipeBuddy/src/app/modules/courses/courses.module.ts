import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ManageCoursesComponent } from './components/manage-courses/manage-courses.component';
import { ButtonModule } from 'primeng/button';
import { ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { CoursesDialogComponent } from './components/manage-courses/components/courses-dialog/courses-dialog.component';
import { ViewCoursesComponent } from './components/view-courses/view-courses.component';
import { CourseDialogComponent } from './components/view-courses/components/course-dialog/course-dialog.component';
import { CourseCardComponent } from './components/view-courses/components/course-card/course-card.component';
import { CardModule } from 'primeng/card';

@NgModule({
  declarations: [
    ManageCoursesComponent,
    CoursesDialogComponent,
    ViewCoursesComponent,
    CourseDialogComponent,
    CourseCardComponent
  ],
  imports: [
    CommonModule,
    TableModule,
    DialogModule,
    ButtonModule,
    ReactiveFormsModule,
    InputTextModule,
    InputTextareaModule,
    CardModule
  ],
  exports: [
    ManageCoursesComponent
  ]
})
export class CoursesModule { }
