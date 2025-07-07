import { Component, OnInit } from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from "@angular/router";
import { PageHeader } from './features/page-header/page-header';
import { PageFooter } from './features/page-footer/page-footer';
import { isUserLogged } from './utils/user.utils';
import { CommonModule } from '@angular/common';



@Component({
  selector: "app-root",
  standalone: true,
  imports: [
    RouterOutlet,
    PageHeader,
    PageFooter,
    CommonModule
  ],
  styleUrls: ['./app.component.css'],
  templateUrl: "./app.component.html"
})
export class AppComponent implements OnInit {
  isUserLogged: boolean = true;
  showNav: boolean = true;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.isUserLogged = isUserLogged();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const hiddenRoutes = ['/login', '/register'];
        this.showNav = !hiddenRoutes.includes(this.router.url);
      }
    });
  }
}
