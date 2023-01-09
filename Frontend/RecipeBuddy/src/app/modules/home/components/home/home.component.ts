import { Component, OnDestroy, OnInit } from '@angular/core';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

  alive: boolean;

  constructor() {}

  ngOnInit(): void {
    this.alive = true;
  }

  ngOnDestroy(): void {
    this.alive = false;
  }
}
