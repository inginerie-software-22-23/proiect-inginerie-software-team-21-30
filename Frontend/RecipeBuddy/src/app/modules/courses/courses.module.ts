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
import { CardModule } from 'primeng/card';
import { SharedModule } from '../shared/shared.module';
import { ViewSubscriptionsComponent } from './components/view-subscriptions/view-subscriptions.component';

@NgModule({
  declarations: [
    ManageCoursesComponent,
    CoursesDialogComponent,
    ViewCoursesComponent,
    ViewSubscriptionsComponent
  ],
  imports: [
    CommonModule,
    TableModule,
    DialogModule,
    ButtonModule,
    ReactiveFormsModule,
    InputTextModule,
    InputTextareaModule,
    CardModule,
    SharedModule
  ],
  exports: [
    ManageCoursesComponent
  ]
})
export class CoursesModule { }
