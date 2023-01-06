import { Component, EventEmitter, Input, OnDestroy, OnInit, OnChanges, Output, SimpleChanges } from '@angular/core';
import { ICourse } from 'src/app/models/course.interface';

@Component({
  selector: 'course-dialog',
  templateUrl: './course-dialog.component.html',
  styleUrls: ['./course-dialog.component.scss']
})
export class CourseDialogComponent implements OnInit, OnDestroy, OnChanges {

  @Input() course: ICourse;
  @Input() visible: boolean;
  @Output() onCloseDialog = new EventEmitter<boolean>();

  alive = true;

  constructor() {}

  ngOnInit(): void {}

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
