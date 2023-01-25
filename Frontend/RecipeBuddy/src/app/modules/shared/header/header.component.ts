import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { filter, take, takeWhile } from 'rxjs';
import { IUser } from 'src/app/models/user.interface';
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
  isMentor: boolean;
  constructor(private router: Router, private _authService: AuthService, private _userService: UserService) { }

  ngOnInit(): void {
    this._authService.getToken()
      .pipe(
        takeWhile(() => this.alive)
      )
      .subscribe(tkn => { this.token = tkn }
      );
    this._userService.getUser()
      .pipe(
        takeWhile(() => this.alive)
      ).subscribe((user: IUser) => this.checkIfUserIsMentor(user));
  }

  navigateHome(){
    this.router.navigate(["/home"]);
  }

  logout() {
    this._authService.setToken();
    this.router.navigate(['/home']);
    this._userService.removeUser();
  }

  isUserLoggedIn() {
    //return this._userService.isLoggedIn();
    return true;
  }

  checkIfUserIsMentor(user: IUser) {
    if(!user){
      this.isMentor = false;
      return;
    }
    this.isMentor = !!user.roles.filter(role => role.name === 'ROLE_MENTOR').length;
  }

  ngOnDestroy(): void {
    this.alive = false;
  }

}
