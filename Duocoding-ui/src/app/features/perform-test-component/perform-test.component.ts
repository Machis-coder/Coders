import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../interfaces/user';
import { BasePage } from '../base.page';
import { isOkResponse, loadResponseData } from 'src/app/core/services/utils.service';
import { FormsModule } from '@angular/forms';
import { ButtonComponent } from 'src/app/shared/components/button-component/button-component';
import { Question } from 'src/app/interfaces/question';
import { CommonModule } from '@angular/common';
import { HomeNavigationComponent } from 'src/app/shared/components/home-navigation-component/home-navigation-component';
import { TestExecutionComponent } from 'src/app/shared/components/test-execution-component/test-execution-component';
import { TestExecutionService } from 'src/app/core/services/test-execution.service';
import { TestService } from 'src/app/core/services/test.service';
import { Test } from 'src/app/interfaces/test';
import ConstRoutes from 'src/app/shared/contants/const-routes';

@Component({
  selector: 'app-perform-test',
  imports: [
      CommonModule, FormsModule, ButtonComponent, TestExecutionComponent,  HomeNavigationComponent
  ],
  templateUrl: './perform-test.component.html',
  styleUrl: './perform-test.component.css'
})
export class PerformTestComponent extends BasePage  {
  route: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router)

  testService: TestService = inject(TestService);
  testExecutionService: TestExecutionService = inject(TestExecutionService);

  id: number = null;
  name: string = null;
  description: string = null;
  questions: Question[] = []; 
  subject: string;


  ngOnInit(): void {
    super.ngOnInit();
    let id =this.getIdToEdit();
    if (id) {
      this.loadTest(id);
    }
    
  }

  loadTest(id: number) {
    this.testService.findTestToPerform(id).subscribe({
        next: (response) => {
            if (isOkResponse(response)) {
              let test = loadResponseData(response);
              this.id = test.id;
              this.subject = test.subject,
              this.name = test.name;
              this.description = test.description;
              this.questions = test.questions;              
            }
        }
      });
  }

  get isnew(): boolean {
    return this.id === undefined || this.id === null;
  } 


  onSave(test: Test) {

    alert(JSON.stringify(test));
    let testExecution =  {
        testId: test.id,
        userId: this.getUserId(),
        responses: test.questions.map(q => {
          return {questionId: q.id, answer: q.answer}
        })
    }
    alert(JSON.stringify(testExecution));
    this.testExecutionService.save(testExecution).subscribe({
      next: (response) => {
        if (isOkResponse(response)) {
          this.navigateTo(ConstRoutes.PATH_HOME);
        }
      },
      error: (err) => {
        alert(err);
      }
    })
  }

  
}
