import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { CoursesDialogComponent } from './components/courses-dialog/courses-dialog.component';
import { ManageCoursesComponent } from './components/manage-courses/manage-courses.component';
import { ButtonModule } from 'primeng/button';
import { ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';




@NgModule({
  declarations: [
    ManageCoursesComponent,
    CoursesDialogComponent
  ],
  imports: [
    CommonModule,
    TableModule,
    DialogModule,
    ButtonModule,
    ReactiveFormsModule,
    InputTextModule,
    InputTextareaModule
  ],
  exports: [
    ManageCoursesComponent
  ]
})
export class CoursesModule { }
