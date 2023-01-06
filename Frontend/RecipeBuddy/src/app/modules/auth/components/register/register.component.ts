import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { catchError, take, takeWhile } from 'rxjs/operators';
import { NotificationsService } from 'src/app/services/notifications.service';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { NotificationType } from 'src/app/enums/notifications.enum';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit, OnDestroy {
  isMentorValue = false;
  registerForm: FormGroup;
  alive: boolean;

  constructor(private _authService: AuthService,
    private _notificationsService: NotificationsService,
    private _router: Router) {
    this.registerForm = new FormGroup({
      name: new FormControl(null, [Validators.required, Validators.pattern('^[a-zA-Z0-9]+$'), Validators.maxLength(20)]),
      password: new FormControl(null, [Validators.required, Validators.minLength(8), Validators.maxLength(30)]),
      email: new FormControl(null, [Validators.required, Validators.email]),
      isMentor: new FormControl(null),
    });
  }

  get name(): FormControl {
    return this.registerForm.get('name') as FormControl;
  }

  get password(): FormControl {
    return this.registerForm.get('password') as FormControl;
  }

  get email(): FormControl {
    return this.registerForm.get('email') as FormControl;
  }

  get isMentor(): FormControl {
    return this.registerForm.get('isMentor') as FormControl;
  }

  ngOnInit(): void {
    this.alive = true;
  }

  submitRegister() {
    const isFormValid = this.registerForm.valid;

    if (isFormValid) {
      const payload = this.registerForm.getRawValue();
      if (payload.isMentor) {
        payload['roles'] = [{
          name: "MENTOR"
        }];
      } else {
        payload['roles'] = [{
          name: "TRAINEE"
        }];
      }
      delete payload.isMentor;
      this._authService.register(payload).pipe(
        take(1),
        takeWhile(() => this.alive),
        catchError((error) => this.handleRegisterError(error))
      ).subscribe((res: any) => {
        if (res) {
          this._notificationsService.createMessage(NotificationType.SUCCESS, 'Register', 'Account created.');
          setTimeout(() => {
            this._router.navigate(['/login']);
          }, 2000);
        }
      });
    }
  }

  handleRegisterError(error): Observable<any> {
    const errorMessage = (error?.error?.message) || 'There was an error on the server.';

    this._notificationsService.createMessage(NotificationType.ERROR, 'Register', errorMessage);
    return of(null);
  }

  ngOnDestroy(): void {
    this.alive = false;
  }
}
