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

    deleteUser(id: number){
        return this._http.delete(Endpoints.USER + `/delete/${id}`);
    }

    updateUser(user: IUser){
        return this._http.put(Endpoints.USER + `/update/${user.id}`, {
            name: user.name,
            password: user.password,
            email: user.email
        });
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
        const user = this.userValue.getValue();
        
        if (token && user && user.name) return true;
        return false;
    }

    removeUser() {
        this.userValue.next(null);
        localStorage.removeItem('User');
    }
}