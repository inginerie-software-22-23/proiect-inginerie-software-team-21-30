import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { takeWhile } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
  alive = true;
  token: string;
  constructor(private router: Router, private _authService: AuthService, private _userService: UserService) { }

  ngOnInit(): void {
    this._authService.getToken()
      .pipe(
        takeWhile(() => this.alive)
      )
      .subscribe(tkn => { this.token = tkn }
      );
  }

  logout() {
    this._authService.setToken("");
    this.router.navigate(['/login']);
    this._userService.removeUser();
  }

  isUserLoggedIn() {
    return this._userService.isLoggedIn();
  }

  ngOnDestroy(): void {
    this.alive = false;
  }

}
