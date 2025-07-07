import { Component, inject, OnInit } from '@angular/core';
import { User } from '../../interfaces/user';
import { CommonModule } from '@angular/common';
import { TableComponent } from 'src/app/shared/components/table-component/table-component';
import { UserService } from 'src/app/core/services/user.service';
import {  
  isOkResponse,
  loadResponseData
} from "../../core/services/utils.service";
import { BasePage } from '../base.page';
import { Router } from '@angular/router';
import { HomeNavigationComponent } from 'src/app/shared/components/home-navigation-component/home-navigation-component';
import {TestService} from "../../core/services/test.service";
import {Test} from "../../interfaces/test";
import ConstRoutes from "../../shared/contants/const-routes";
import {ButtonComponent} from "../../shared/components/button-component/button-component";


@Component({
  selector: 'app-tests',
  standalone: true,
  imports: [CommonModule, TableComponent, HomeNavigationComponent, ButtonComponent],
  templateUrl: './tests.component.html',
  styleUrl: './tests.component.css'
})
export class TestsComponent extends BasePage {
  list: Test[] = [];
  
  router: Router = inject(Router);
  testService: TestService = inject(TestService);


  ngOnInit() {
      super.ngOnInit();
      this.loadUsers();
  }
  
  loadUsers() {
    this.testService.findAll().subscribe({
      next: (response) => {
        if (isOkResponse(response)) {
          this.list = loadResponseData(response);
        } else {
          //this.error = loadResponseError(response);
        }
      },
      error: (err) =>{
        console.log(err);
      }
      
    });      
  } 

  gotoEdit(id) {
    this.navigateTo(ConstRoutes.PATH_NEW_TEST + '/'+ id);
  }

  gotoNew() {
    this.navigateTo(ConstRoutes.PATH_NEW_TEST);
  }

}

