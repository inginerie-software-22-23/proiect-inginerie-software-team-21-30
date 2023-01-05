import { Component, OnDestroy, OnInit } from '@angular/core';
import { ICourse } from 'src/app/models/course.interface';
import { CoursesService } from 'src/app/services/courses.service';
import { catchError, take, takeWhile } from 'rxjs/operators'
import { NotificationsService } from 'src/app/services/notifications.service';
import { NotificationType } from 'src/app/enums/notifications.enum';
import { Observable, of } from 'rxjs';
import { Modes } from 'src/app/enums/modes.enum';
import { UserService } from 'src/app/services/user.service';
import { IUser } from 'src/app/models/user.interface';

@Component({
  selector: 'app-manage-courses',
  templateUrl: './manage-courses.component.html',
  styleUrls: ['./manage-courses.component.scss']
})
export class ManageCoursesComponent implements OnInit, OnDestroy {
  alive = true;
  courses: ICourse[];
  selectedCourse: ICourse;
  selectedMode: string;
  loading = true;
  actionModes = Modes;
  user: IUser;
  constructor(private coursesService: CoursesService, private _notificationsService: NotificationsService, private _userService: UserService) { }

  ngOnInit(): void {
    this._userService.getUser().pipe(
      takeWhile(() => this.alive)
    ).subscribe((res: IUser) => {
      if (!res) {
        return;
      }
      this.user = res;
      this.getCourses();
    })
  }

  deleteCourse(id: number) {
    if (!id) {
      return;
    }
    this.loading = true;
    this.coursesService.delete(id).pipe(
      takeWhile(() => this.alive),
      catchError(err => this.handleCoursesError(err))
    ).subscribe((res: any) => {
      if (!res) {
        return;
      }
      this.getCourses();
      this._notificationsService.createMessage(NotificationType.SUCCESS, 'Courses', 'Course deleted.');
    })
  }

  addCourse() {
    this.selectedMode = Modes.ADD;
  }

  openDialog(course: ICourse, mode: string) {
    this.selectedCourse = course;
    this.selectedMode = mode;
  }

  handleCoursesError(error): Observable<any> {
    const errorMessage = (error?.error?.message) || 'There was an error on the server.';
    this._notificationsService.createMessage(NotificationType.ERROR, 'Courses', errorMessage);
    this.loading = false;
    return of(null);
  }

  getCourses() {
    this.loading = true;
    this.coursesService.getCoursesByMentor(this.user.name).pipe(
      takeWhile(() => this.alive),
      catchError(err => this.handleCoursesError(err))
    ).subscribe((res: ICourse[]) => {
      this.courses = res;
      this.loading = false;
    });
  }

  onDialogClose(message?) {
    this.selectedCourse = null;
    this.selectedMode = null;
    if (message) {
      this.getCourses();
    }
  }

  ngOnDestroy(): void {
    this.alive = false;
  }

}
