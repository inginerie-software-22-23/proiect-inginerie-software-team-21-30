import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { Endpoints } from "../enums/endpoints.enum";
import { ICourse } from "../models/course.interface";
import { IRecipe } from "../models/recipe.interface";

@Injectable({
    providedIn: 'root'
})
export class RecipesService {
    constructor(private _http: HttpClient) { }

    getRecipesByUser(name: string) {
        return this._http.get(Endpoints.RECIPES + `/recipes/user/${name}`);
    }

    read(id: number) {
        return this._http.get(Endpoints.RECIPES + `/${id}`);
    }

    get(name?: string) {
        return this._http.get(Endpoints.RECIPES + '/recipes' + `${name ? '/' + name : ''}`);
    }

    delete(id: number) {
        return this._http.delete(Endpoints.RECIPES + `/delete/${id}`, { responseType: 'text' });
    }

    edit(recipe: IRecipe) {
        return this._http.put(Endpoints.RECIPES + `/update/${recipe.id}`, recipe, { responseType: 'text' });
    }

    add(recipe: IRecipe, userId: number) {
        return this._http.post(Endpoints.RECIPES + `/create/${userId}`, recipe, { responseType: 'text' });
    }
}