import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { getUser } from 'src/app/core/services/utils.service';
import {HomeNavigationComponent} from "../../shared/components/home-navigation-component/home-navigation-component";

@Component({
  selector: 'page-header',
  imports: [RouterModule, CommonModule, HomeNavigationComponent],
  templateUrl: './page-header.html',
  styleUrl: './page-header.css'
})
export class PageHeader implements OnInit {
  user: string = '';

  router: Router = inject(Router);

  ngOnInit(): void {
    const user = getUser();
    this.user = user !== undefined && user !== null? user.name + ' ' + user.surname + ' as ' + user.role: '';
  }
  title: string = "DuoCoding";

  onLogout() {
    if (confirm("Está seguro de cerrar sessión")) {
      localStorage.clear();
      this.router.navigate(['/']);
    }
  }
}
