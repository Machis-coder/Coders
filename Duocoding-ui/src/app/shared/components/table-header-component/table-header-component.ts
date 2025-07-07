import { Component } from '@angular/core';
import {HomeNavigationComponent} from "../home-navigation-component/home-navigation-component";

@Component({
  selector: 'table-header',
  imports: [
    HomeNavigationComponent
  ],
  templateUrl: './table-header-component.html',
  styleUrl: './table-header-component.css'
})
export class TableHeaderComponent {

}
