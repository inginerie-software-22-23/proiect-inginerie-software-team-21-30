import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, catchError, take } from "rxjs";
import { Endpoints } from "../enums/endpoints.enum";
import { IUser } from "../models/user.interface";

@Injectable({
    providedIn: 'root'
})
export class UserService {
    userValue = new BehaviorSubject<IUser>(null);
    constructor(private _http: HttpClient) { }

    findUser(username: string) {
        return this._http.get(Endpoints.USER + `/name/${username}`);
    }

    refreshUser() {
        const username = localStorage.getItem("User");
        if (!username) {
            return;
        }
        this.findUser(username).pipe(
            take(1),
            catchError(err => err)
        ).subscribe((res: IUser) => {
            if (!res) {
                return;
            }
            this.userValue.next(res);
        });
    }

    setUser(user: IUser) {
        localStorage.setItem("User", user.name);
        this.userValue.next(user);
    }

    getUser() {
        return this.userValue.asObservable();
    }

    isLoggedIn() {
        const token = localStorage.getItem('Token');
        
        if (token && !!this.userValue.getValue()) return true;
        return false;
    }

    removeUser() {
        this.userValue.next(null);
        localStorage.setItem("User",'')
    }
}