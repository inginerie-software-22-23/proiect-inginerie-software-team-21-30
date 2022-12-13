import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, Observable, of, take, takeWhile } from 'rxjs';
import { NotificationType } from 'src/app/enums/notifications.enum';
import { AuthService } from 'src/app/services/auth.service';
import { NotificationsService } from 'src/app/services/notifications.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  loginForm: FormGroup;
  alive: boolean;

  constructor(private _authService: AuthService,
    private _notificationsService: NotificationsService,
    private _router: Router) {
    this.loginForm = new FormGroup({
      name: new FormControl(null, [Validators.required, Validators.pattern('^[a-zA-Z0-9]+$'), Validators.maxLength(20)]),
      password: new FormControl(null, [Validators.required, Validators.minLength(8), Validators.maxLength(30)])
    });
  }
  get name(): FormControl {
    return this.loginForm.get('name') as FormControl;
  }

  get password(): FormControl {
    return this.loginForm.get('password') as FormControl;
  }

  ngOnInit(): void {
    this.alive = true;
  }


  submitLogin() {
    const isFormValid = this.loginForm.valid;
    console.log(this.loginForm);

    if (isFormValid) {
      const payload = this.loginForm.getRawValue();

      this._authService.login(payload).pipe(
        take(1),
        takeWhile(() => this.alive),
        catchError((error) => this.handleLoginError(error))
      ).subscribe((res: any) => {
        if (res && res.jwt) {
          this._notificationsService.createMessage(NotificationType.SUCCESS, 'Login', 'Login successful.');
          this._authService.setToken(res.jwt);
          setTimeout(() => {
            this._router.navigate(['/home']);
          }, 500);
        }
      });
    }
  }

  handleLoginError(error): Observable<any> {
    const errorMessage = (error?.error?.message) || 'There was an error on the server.';

    this._notificationsService.createMessage(NotificationType.ERROR, 'Register', errorMessage);
    return of(null);
  }

  ngOnDestroy(): void {
    this.alive = false;
  }
}
