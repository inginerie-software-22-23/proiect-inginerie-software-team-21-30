import { Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { catchError, Observable, of, take } from 'rxjs';
import { Modes } from 'src/app/enums/modes.enum';
import { NotificationType } from 'src/app/enums/notifications.enum';
import { IRecipe } from 'src/app/models/recipe.interface';
import { IUser } from 'src/app/models/user.interface';
import { NotificationsService } from 'src/app/services/notifications.service';
import { RecipesService } from 'src/app/services/recipes.service';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-recipe-manage',
  templateUrl: './recipe-manage.component.html',
  styleUrls: ['./recipe-manage.component.scss']
})
export class RecipeManageComponent implements OnInit, OnDestroy, OnChanges {
  @Input() recipe: IRecipe;
  @Input() visible: boolean;
  @Input() mode: string;
  @Input() user: IUser;
  @Output() dialogClosed = new EventEmitter<string>();
  recipesForm: FormGroup;
  alive = true;
  actionModes = Modes;
  constructor(private _recipesService: RecipesService, private _notificationsService: NotificationsService) {
    this.recipesForm = new FormGroup({
      id: new FormControl(null),
      name: new FormControl(null, [Validators.required, Validators.maxLength(50)]),
      image: new FormControl(null, [Validators.required]),
      description: new FormControl(null, [Validators.required, Validators.maxLength(10000)]),
      ingredients: new FormControl(null, [Validators.required, Validators.maxLength(10000)]),
      estimatedPrepTimeInMinutes: new FormControl(null, [Validators.required]),
    });
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['recipe']) {
      if (this.recipe) {
        this.recipesForm.patchValue(this.recipe);
      } else {
        this.recipesForm.reset();
      }
    }
    if (changes['mode']) {
      console.log(this.mode)
      this.recipesForm.enable();
    }
  }

  handleImageUpload(event) {
    let uploadedImageBase64: any;
    const reader = new FileReader();

    reader.addEventListener("load", () => {
      uploadedImageBase64 = reader.result;
      this.recipesForm.get('image').setValue(uploadedImageBase64.split(',')[1]);
    }, false);

    if (event.target.files[0]) {
      reader.readAsDataURL(event.target.files[0]);
    }
  }

  saveCourse() {
    if (!this.recipesForm.valid) {
      return;
    }
    console.log(this.recipesForm.getRawValue())
    const recipe: IRecipe = this.recipesForm.getRawValue();
    if (this.mode === Modes.EDIT) {
      this.editRecipe(recipe);
    } else {
      delete recipe.id;
      this.addRecipe(recipe);
    }
  }

  addRecipe(recipe: IRecipe) {
    if (!this.user.id) {
      return;
    }
    this._recipesService.add(recipe, this.user.id).pipe(
      take(1),
      catchError(err => this.handleRecipesError(err))
    ).subscribe(
      res => {
        if (!res) {
          return;
        }
        this.hideDialog('refresh');
        this._notificationsService.createMessage(NotificationType.SUCCESS, 'Recipes', res);
      }
    )
  }

  editRecipe(recipe: IRecipe) {
    this._recipesService.edit(recipe).pipe(
      take(1),
      catchError(err => this.handleRecipesError(err))
    ).subscribe(
      res => {
        if (!res) {
          return;
        }
        this.hideDialog('refresh');
        this._notificationsService.createMessage(NotificationType.SUCCESS, 'recipes', res);
      }
    )
  }

  handleRecipesError(error): Observable<any> {
    const errorMessage = (error?.error?.message) || 'There was an error on the server.';
    this._notificationsService.createMessage(NotificationType.ERROR, 'Recipes', errorMessage);
    return of(null);
  }

  hideDialog(message?: string) {
    this.recipesForm.reset();
    this.dialogClosed.emit(message);
  }

  ngOnDestroy(): void {
    this.alive = false;
  }
}

