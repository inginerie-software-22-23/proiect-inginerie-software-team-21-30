import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Endpoints } from "../enums/endpoints.enum";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    constructor(private _http: HttpClient) {}

    register(payload: { name: string, password: string, email: string }) {
        return this._http.post(Endpoints.REGISTER, payload);
    }
}