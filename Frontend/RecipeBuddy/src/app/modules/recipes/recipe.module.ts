import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { TableModule } from 'primeng/table';
import { RecipeCardComponent } from './components/view-recpies/components/recipe-card/recipe-card.component';
import { RecipeDialogComponent } from './components/view-recpies/components/recipe-dialog/recipe-dialog.component';
import { RecipeManageComponent } from './components/view-recpies/components/recipe-manage/recipe-manage.component';
import { ViewRecipesComponent } from './components/view-recpies/view-recipes.component';

@NgModule({
  declarations: [
    RecipeCardComponent,
    ViewRecipesComponent,
    RecipeDialogComponent,
    RecipeManageComponent
  ],
  imports: [
    CommonModule,
    TableModule,
    DialogModule,
    ButtonModule,
    ReactiveFormsModule,
    InputTextModule,
    InputTextareaModule,
    CardModule
  ],
  exports: [
    ViewRecipesComponent
  ]
})
export class RecipesModule { }
