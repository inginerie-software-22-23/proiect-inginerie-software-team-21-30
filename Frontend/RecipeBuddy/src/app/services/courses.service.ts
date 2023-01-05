import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { Endpoints } from "../enums/endpoints.enum";
import { ICourse } from "../models/course.interface";

@Injectable({
    providedIn: 'root'
})
export class CoursesService {
    constructor(private _http: HttpClient) { }

    getCoursesByMentor(name: string) {
        return this._http.get(Endpoints.COURSES + `/mentor/${name}`);
    }

    read(id: number) {
        return this._http.get(Endpoints.COURSE + id);
    }

    get(name?: string) {
        return this._http.get(Endpoints.COURSES + `${name ? '/' + name : ''}`);
    }

    delete(id: number) {
        return this._http.delete(Endpoints.COURSE + `/delete/${id}`, { responseType: 'text' });
    }

    edit(course: ICourse) {
        return this._http.put(Endpoints.COURSE + `/update/${course.id}`, course, { responseType: 'text' });
    }

    add(course: ICourse, userId: number) {
        return this._http.post(Endpoints.COURSE + `/create/${userId}`, course, { responseType: 'text' });
    }
}