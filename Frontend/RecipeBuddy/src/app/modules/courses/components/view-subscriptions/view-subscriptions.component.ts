import { Component, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, catchError, distinctUntilChanged, Observable, of, take, takeWhile } from 'rxjs';
import { NotificationType } from 'src/app/enums/notifications.enum';
import { ICourse } from 'src/app/models/course.interface';
import { IUser } from 'src/app/models/user.interface';
import { CoursesService } from 'src/app/services/courses.service';
import { NotificationsService } from 'src/app/services/notifications.service';
import { UserService } from 'src/app/services/user.service';
@Component({
  selector: 'app-view-subscriptions',
  templateUrl: './view-subscriptions.component.html',
  styleUrls: ['./view-subscriptions.component.scss']
})
export class ViewSubscriptionsComponent implements OnInit, OnDestroy {

  alive: boolean;
  user: IUser;
  subscriptions = new BehaviorSubject<ICourse[]>([]);
  selectedSubscription = new BehaviorSubject<ICourse>(null);
  isDialogVisible = new BehaviorSubject<boolean>(false);

  constructor(private _coursesService: CoursesService, private _userService: UserService, private _notificationsService: NotificationsService) {}

  ngOnInit(): void {
    this.alive = true;

    this._userService.getUser().pipe(
      takeWhile(() => this.alive)
    ).subscribe((user: IUser) => {
      if (user) {
        this.user = user;
        this.getSubscriptions(this.user.name);
      }
    });
  }

  getSubscriptions(name: string) {
    this._coursesService.getSubscriptions(name).pipe(
      takeWhile(() => this.alive),
      distinctUntilChanged()
    ).subscribe((subscriptions: ICourse[]) => {
      this.subscriptions.next(subscriptions && subscriptions.length ? this.mapSubscriptions(subscriptions) : []);
    });
  }

  mapSubscriptions(subscriptions): ICourse[] {
    return subscriptions.map(subscription => {
      return { ...subscription.course, subscriptionId: subscription.id, subscriptionJoinDate: subscription.joinDate };
    });
  }

  onDialogClose() {
    this.isDialogVisible.next(false);
  }

  unsubscribeFromCourse(id: number) {
    if (!id) return;

    this._coursesService.unsubscribe(this.user.id, id).pipe(
      take(1),
      takeWhile(() => this.alive),
      catchError(err => this.handleSubscriptionsError(err))
    ).subscribe((res: any) => {
      if (!res) return;

      this.getSubscriptions(this.user.name);
      this._notificationsService.createMessage(NotificationType.SUCCESS, 'Unsubscription', 'Unsubscription confirmed.');
    });
  }

  handleSubscriptionsError(error): Observable<any> {
    const errorMessage = (error?.error?.message) || 'There was an error on the server.';
    this._notificationsService.createMessage(NotificationType.ERROR, 'Subscription', errorMessage);
    return of(null);
  }

  selectSubscription(subscription: ICourse) {
    this.selectedSubscription.next(subscription);
    this.isDialogVisible.next(true);
  }

  ngOnDestroy(): void {
    this.alive = false;
  }
}
