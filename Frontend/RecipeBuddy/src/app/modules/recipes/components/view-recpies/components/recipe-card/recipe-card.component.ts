import { Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { BehaviorSubject, take, takeWhile } from 'rxjs';
import { NotificationType } from 'src/app/enums/notifications.enum';
import { IRecipe } from 'src/app/models/recipe.interface';
import { IUser } from 'src/app/models/user.interface';
import { NotificationsService } from 'src/app/services/notifications.service';
import { RecipesService } from 'src/app/services/recipes.service';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-recipe-card',
  templateUrl: './recipe-card.component.html',
  styleUrls: ['./recipe-card.component.scss']
})
export class RecipeCardComponent implements OnInit, OnDestroy, OnChanges {
  @Input() recipe: IRecipe;
  @Input() mode: string;
  @Output() onSelectRecipe = new EventEmitter<IRecipe>();
  @Output() onRefreshRecipes = new EventEmitter<boolean>(null);
  selectedMode = new BehaviorSubject<string>(null);
  alive: boolean;
  username: string;

  constructor(private _userService: UserService, private _sanitizer: DomSanitizer, private _recipesService: RecipesService, private _notificationsService: NotificationsService) { }

  ngOnInit(): void {
    this.alive = true;

    this._userService.getUser().pipe(
      takeWhile(() => this.alive),
    ).subscribe((user: IUser) => {
      this.username = user.name;
    });
  }

  selectRecipe() {
    this.getRecipe()
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['mode']) {
      this.selectedMode.next(changes['mode'].currentValue);
    }
  }

  deleteRecipe() {
    if (!this.recipe.id) {
      return;
    }
    this._recipesService.delete(this.recipe.id).pipe(
      take(1)
    ).subscribe(res => {
      if (res) {
        this._notificationsService.createMessage(NotificationType.SUCCESS, 'Recipes', res);
        this.onRefreshRecipes.emit(true)
      }
    })
  }

  getRecipe() {
    this._recipesService.read(this.recipe.id).pipe(
      take(1)
    ).subscribe((recipe: IRecipe) =>
      this.onSelectRecipe.emit(recipe));
  }


  getRecipeImage() {
    return this._sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,'
      + this.recipe.image);
  }

  isUserLoggedIn() {
    return this._userService.isLoggedIn();
  }

  ngOnDestroy(): void {
    this.alive = false;
  }

}
