import { Component, OnDestroy, OnInit, OnChanges, Input, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { ICourse } from 'src/app/models/course.interface';


@Component({
  selector: 'app-course-card',
  templateUrl: './course-card.component.html',
  styleUrls: ['./course-card.component.scss']
})
export class CourseCardComponent implements OnInit, OnDestroy, OnChanges {

  @Input() course: ICourse;
  @Output() onSelectCourse = new EventEmitter<ICourse>();
  @Output() onSubscribeToCourse = new EventEmitter<number>();

  alive: boolean;

  constructor() {}

  ngOnInit(): void {
    this.alive = true;
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes && changes.course && changes.course.currentValue) {
      this.course = changes.course.currentValue;
    }
  }

  selectCourse() {
    this.onSelectCourse.emit(this.course);
  }

  subscribeToCourse() {
    if (this.course && this.course.id) {
      this.onSubscribeToCourse.emit(this.course.id);
    }
  }

  ngOnDestroy(): void {
    this.alive = false;
  }

}
