import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { take, takeWhile, catchError, Observable, of } from 'rxjs';
import { NotificationType } from 'src/app/enums/notifications.enum';
import { IUser } from 'src/app/models/user.interface';
import { AuthService } from 'src/app/services/auth.service';
import { NotificationsService } from 'src/app/services/notifications.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.scss']
})
export class ProfilePageComponent implements OnInit, OnDestroy {

  editForm: FormGroup;
  alive: boolean;
  user: IUser;

  constructor(private _authService: AuthService,
    private _notificationsService: NotificationsService,
    private _userService: UserService,
    private _router: Router) {
    this.editForm = new FormGroup({
      name: new FormControl("", [Validators.required, Validators.pattern('^[a-zA-Z0-9]+$'), Validators.maxLength(20)]),
      email: new FormControl("", [Validators.required])
    });
  }
  get name(): FormControl {
    return this.editForm.get('name') as FormControl;
  }

  get email(): FormControl {
    return this.editForm.get('password') as FormControl;
  }

  ngOnInit(): void {
    this.alive = true;

    this._userService.getUser().pipe(
      takeWhile(() => this.alive),
    ).subscribe((user: IUser) => {
      if (user) {
        this.user = user;
        this.editForm.patchValue({
          name: this.user.name,
          email: this.user.email
        });
      }
    });
  }


  deleteAccount() {
    this._userService.deleteUser(this.user.id).pipe(
      takeWhile(() => this.alive),
    ).subscribe((res) => {
      localStorage.removeItem('Token');
      localStorage.removeItem('User');
      this._notificationsService.createMessage(NotificationType.SUCCESS, "User", "User deleted.");
      setTimeout(() => {
        this._router.navigate(['/login']);
      }, 500);
    });
  }

  saveChanges() {
    if (this.user.name !== this.editForm.getRawValue().name || 
        this.user.email !== this.editForm.getRawValue().email) {
          this._userService.updateUser({...this.editForm.getRawValue(), id: this.user.id}).pipe(
            takeWhile(() => this.alive),
            catchError(err => this.handleLoginError(err))
          ).subscribe((res) => {
            if (res === "Username already taken.") {
              this._notificationsService.createMessage(NotificationType.ERROR, 'Profile', "Username already taken.");
            } else {
              this._notificationsService.createMessage(NotificationType.SUCCESS, "User", "Profile updated.");
              if (this.user.name !== this.editForm.getRawValue().name) {
                localStorage.removeItem('Token');
                localStorage.removeItem('User');
                this._router.navigate(['/login']);
              } else {
                this.user.email = this.editForm.getRawValue().email;
                this.editForm.patchValue({
                  email: this.editForm.getRawValue().email
                });
              }
            }
          });
        }
  }

  getUser(username: string) {
    this._userService.findUser(username).pipe(
      take(1),
      catchError(err => this.handleLoginError(err))
    ).subscribe((res: IUser) => {
      if (!res) {
        return;
      }
      this._userService.setUser(res);
    });
  }

  handleLoginError(error): Observable<any> {
    const errorMessage = (error?.error?.message) || 'There was an error on the server.';
    this._notificationsService.createMessage(NotificationType.ERROR, 'Profile', errorMessage);
    return of(null);
  }

  ngOnDestroy(): void {
    this.alive = false;
  }
}
