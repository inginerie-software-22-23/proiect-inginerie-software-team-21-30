import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { take } from 'rxjs';
import { IUser } from '../models/user.interface';
import { UserService } from '../services/user.service';

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private router: Router, private _userService: UserService) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        let userRoles
        this._userService.getUser().pipe(take(1)).subscribe((user: IUser) => userRoles = user.roles)

        if (userRoles) {
            return true;
        }

        this.router.navigate(['/home']);
        return false;
    }
}