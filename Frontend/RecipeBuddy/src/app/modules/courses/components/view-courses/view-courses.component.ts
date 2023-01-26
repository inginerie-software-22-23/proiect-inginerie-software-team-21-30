import { Component, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, catchError, distinctUntilChanged, Observable, of, take, takeWhile } from 'rxjs';
import { NotificationType } from 'src/app/enums/notifications.enum';
import { ICourse } from 'src/app/models/course.interface';
import { IUser } from 'src/app/models/user.interface';
import { CoursesService } from 'src/app/services/courses.service';
import { NotificationsService } from 'src/app/services/notifications.service';
import { UserService } from 'src/app/services/user.service';
@Component({
  selector: 'app-view-courses',
  templateUrl: './view-courses.component.html',
  styleUrls: ['./view-courses.component.scss']
})
export class ViewCoursesComponent implements OnInit, OnDestroy {

  alive: boolean;
  user: IUser;
  courses = new BehaviorSubject<ICourse[]>([]);
  selectedCourse = new BehaviorSubject<ICourse>(null);
  isDialogVisible = new BehaviorSubject<boolean>(false);

  constructor(private _coursesService: CoursesService, private _userService: UserService, private _notificationsService: NotificationsService) {}

  ngOnInit(): void {
    this.alive = true;

    this._coursesService.get().pipe(
      takeWhile(() => this.alive),
      distinctUntilChanged()
    ).subscribe((courses: ICourse[]) => {
      this.courses.next(courses && courses.length ? this.mapCourses(courses) : []);
    });

    this._userService.getUser().pipe(
      takeWhile(() => this.alive)
    ).subscribe((user: IUser) => {
      this.user = user;
    });
  }

  onDialogClose() {
    this.isDialogVisible.next(false);
  }

  mapCourses(courses: ICourse[]): ICourse[] {
    return courses.filter((course: ICourse) => this.user ? course.mentor !== this.user.name : true)
                  .map((course: ICourse) => {
      if (!this.user) return { ...course, activeSubscription: false };

      const activeSubscription = course.subscriptions.find(subscription => subscription.user.name === this.user.name);
      return { ...course, activeSubscription: !!activeSubscription };
    });
  }

  subscribeToCourse(id: number) {
    if (!id) return;

    this._coursesService.subscribe(this.user.id, id).pipe(
      take(1),
      takeWhile(() => this.alive),
      catchError(err => this.handleCoursesError(err))
    ).subscribe((res: any) => {
      if (!res) return;

      this.getCourses();
      this._notificationsService.createMessage(NotificationType.SUCCESS, 'Subscription', 'Subscription confirmed.');
    });
  }

  handleCoursesError(error): Observable<any> {
    const errorMessage = (error?.error?.message) || 'There was an error on the server.';
    this._notificationsService.createMessage(NotificationType.ERROR, 'Courses', errorMessage);
    return of(null);
  }

  getCourses() {
    this._coursesService.get().pipe(
      take(1),
      takeWhile(() => this.alive),
      distinctUntilChanged()
    ).subscribe((courses: ICourse[]) => {
      this.courses.next(courses && courses.length ? this.mapCourses(courses) : []);
    });
  }

  selectCourse(course: ICourse) {
    this.selectedCourse.next(course);
    this.isDialogVisible.next(true);
  }

  ngOnDestroy(): void {
    this.alive = false;
  }
}
