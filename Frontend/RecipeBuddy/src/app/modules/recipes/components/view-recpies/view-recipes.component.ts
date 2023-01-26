import { Component, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, filter, Observable, of, takeWhile } from 'rxjs';
import { NotificationType } from 'src/app/enums/notifications.enum';
import { ICourse } from 'src/app/models/course.interface';
import { IRecipe } from 'src/app/models/recipe.interface';
import { IUser } from 'src/app/models/user.interface';
import { NotificationsService } from 'src/app/services/notifications.service';
import { RecipesService } from 'src/app/services/recipes.service';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-view-recipes',
  templateUrl: './view-recipes.component.html',
  styleUrls: ['./view-recipes.component.scss']
})
export class ViewRecipesComponent implements OnInit, OnDestroy {
  alive: boolean;
  user: IUser;
  allRecipes: IRecipe[] = [];
  yourRecipes: IRecipe[] = [];
  selectedRecipe = new BehaviorSubject<IRecipe>(null);
  selectedMode = new BehaviorSubject<string>(null);
  isManageMode = new BehaviorSubject<boolean>(false);
  isDialogVisible = new BehaviorSubject<boolean>(false);

  constructor(private _userService: UserService, private _notificationsService: NotificationsService, private _recipesService: RecipesService) { }

  ngOnInit(): void {
    this.alive = true;

    this._userService.getUser().pipe(
      takeWhile(() => this.alive),
    ).subscribe((user: IUser) => {
      this.user = user;
      this.getUsersRecipes();
    });
  }

  mapRecipes(recipes: IRecipe[]) {
    const yourRecipesIds = this.yourRecipes.map(recipe => recipe.id);
    this.allRecipes = [];
    recipes.forEach(recipe => {
      if (yourRecipesIds.findIndex((id) => id === recipe.id) === -1) {
        this.allRecipes.push(recipe);
      }
    });
  }

  addRecipe() {
    this.selectedMode.next('add');
  }

  refreshRecipes(event) {
    if (event) {
      this.getUsersRecipes();
    }
  }

  selectRecipe(recipe: IRecipe) {
    if (!recipe) {
      return;
    }
    if (this.isManageMode) {
      this.selectedMode.next('edit')
    }
    this.isDialogVisible.next(true);
    this.selectedRecipe.next(recipe);
  }

  getUsersRecipes() {
    if (this.user) {
      this._recipesService.getRecipesByUser(this.user.name).pipe(
        takeWhile(() => this.alive)
      ).subscribe((recipes: IRecipe[]) => {
        this.yourRecipes = recipes;
        this.getAllRecipes();
      });
      return;
    }
    this.getAllRecipes()
  }

  getAllRecipes() {
    this._recipesService.get().pipe(
      takeWhile(() => this.alive)
    ).subscribe((recipes: IRecipe[]) => {
      this.mapRecipes(recipes)
    });
  }

  onDialogClose(message?) {
    this.selectedRecipe.next(null);
    this.selectedMode.next(null);
    if (message) {
      this.getUsersRecipes();
    }
    this.isDialogVisible.next(false);
  }

  switchManageMode() {
    this.isManageMode.next(!this.isManageMode.getValue());
  }

  handleRecipesError(error): Observable<any> {
    const errorMessage = (error?.error?.message) || 'There was an error on the server.';
    this._notificationsService.createMessage(NotificationType.ERROR, 'Recipes', errorMessage);
    return of(null);
  }

  ngOnDestroy(): void {
    this.alive = false;
  }
}
