import { Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Modes } from 'src/app/enums/modes.enum';
import { ICourse } from 'src/app/models/course.interface';
import { CoursesService } from 'src/app/services/courses.service';
import { NotificationsService } from 'src/app/services/notifications.service';
import { take, catchError } from 'rxjs/operators'
import { NotificationType } from 'src/app/enums/notifications.enum';
import { Observable, of } from 'rxjs';
import { IUser } from 'src/app/models/user.interface';

@Component({
  selector: 'courses-dialog',
  templateUrl: './courses-dialog.component.html',
  styleUrls: ['./courses-dialog.component.scss']
})
export class CoursesDialogComponent implements OnInit, OnDestroy, OnChanges {
  @Input() course: ICourse;
  @Input() visible: boolean;
  @Input() mode: string;
  @Input() user: IUser;
  @Output() dialogClosed = new EventEmitter<string>();
  coursesForm: FormGroup;
  alive = true;
  actionModes = Modes;
  constructor(private coursesService: CoursesService, private _notificationsService: NotificationsService) {
    this.coursesForm = new FormGroup({
      id: new FormControl(null),
      name: new FormControl(null, [Validators.required, Validators.maxLength(50)]),
      shortDescription: new FormControl(null, [Validators.required, Validators.maxLength(70)]),
      longDescription: new FormControl(null, [Validators.required, Validators.maxLength(300)]),
      meetLink: new FormControl(null, [Validators.required, Validators.maxLength(50)]),
      subscriptions: new FormControl(null),
    });
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['course']) {
      if (this.course) {
        this.coursesForm.patchValue(this.course);
      } else {
        this.coursesForm.reset();
      }
    }
    if (changes['mode']) {
      if (this.mode === 'view') {
        this.coursesForm.disable();
      } else {
        this.coursesForm.enable();
      }
    }
  }

  saveCourse() {
    if (!this.coursesForm.valid) {
      return;
    }
    const course: ICourse = this.coursesForm.getRawValue();
    delete course.subscriptions;
    if (this.mode === Modes.EDIT) {
      this.editCourse(course);
    } else {
      delete course.id;
      this.addCourse(course);
    }
  }

  addCourse(course: ICourse) {
    delete course.id;
    if (!this.user.id) {
      return;
    }
    this.coursesService.add(course, this.user.id).pipe(
      take(1),
      catchError(err => this.handleCoursesError(err))
    ).subscribe(
      res => {
        if (!res) {
          return;
        }
        this.hideDialog('refresh');
        this._notificationsService.createMessage(NotificationType.SUCCESS, 'Courses', res);
      }
    )
  }

  editCourse(course: ICourse) {
    this.coursesService.edit(course).pipe(
      take(1),
      catchError(err => this.handleCoursesError(err))
    ).subscribe(
      res => {
        if (!res) {
          return;
        }
        this.hideDialog('refresh');
        this._notificationsService.createMessage(NotificationType.SUCCESS, 'Courses', res);
      }
    )
  }

  handleCoursesError(error): Observable<any> {
    const errorMessage = (error?.error?.message) || 'There was an error on the server.';
    this._notificationsService.createMessage(NotificationType.ERROR, 'Courses', errorMessage);
    return of(null);
  }

  hideDialog(message?: string) {
    this.coursesForm.reset();
    this.dialogClosed.emit(message);
  }

  ngOnDestroy(): void {
    this.alive = false;
  }

}
