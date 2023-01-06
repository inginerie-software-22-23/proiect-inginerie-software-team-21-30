import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { takeWhile } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
  alive = true;
  token: string;
  constructor(private router: Router, private _authService: AuthService) { }

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
  }

  ngOnDestroy(): void {
    this.alive = false;
  }

}
