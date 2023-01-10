import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { Endpoints } from "../enums/endpoints.enum";
import { IUser } from "../models/user.interface";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    tokenValue = new BehaviorSubject<string>(null);
    constructor(private _http: HttpClient) { }

    register(payload: { name: string, password: string, email: string }) {
        return this._http.post(Endpoints.REGISTER, payload, { responseType: "text" });
    }

    login(payload: { name: string, password: string }) {
        return this._http.post(Endpoints.LOGIN, payload);
    }

    setToken(token?: string) {
        this.tokenValue.next(token);

        if (token) {
            localStorage.setItem('Token', token);
        } else {
            localStorage.removeItem('Token');
        }
    }

    refreshToken() {
        const token = localStorage.getItem("Token");
        if (!token) {
            return;
        }
        this.tokenValue.next(token);
    }

    getToken() {
        return this.tokenValue.asObservable();
    }
}