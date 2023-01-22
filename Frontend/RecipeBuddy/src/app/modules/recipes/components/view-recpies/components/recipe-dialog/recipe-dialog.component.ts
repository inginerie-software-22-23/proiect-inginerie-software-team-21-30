import { Component, EventEmitter, Input, OnDestroy, OnInit, OnChanges, Output, SimpleChanges } from '@angular/core';
import { ICourse } from 'src/app/models/course.interface';
import { IRecipe } from 'src/app/models/recipe.interface';

@Component({
  selector: 'recipe-dialog',
  templateUrl: './recipe-dialog.component.html',
  styleUrls: ['./recipe-dialog.component.scss']
})
export class RecipeDialogComponent implements OnInit, OnDestroy, OnChanges {
  @Input() recipe: IRecipe;
  @Input() visible: boolean;
  @Output() onCloseDialog = new EventEmitter<boolean>();

  alive = true;

  constructor() { }

  ngOnInit(): void { }

  ngOnChanges(changes: SimpleChanges) {
    if (changes && changes.visible && changes.visible.currentValue) {
      this.visible = changes.visible.currentValue;
    }
  }

  hideDialog() {
    this.onCloseDialog.emit(true);
  }

  ngOnDestroy(): void {
    this.alive = false;
  }
}
