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
import { TestExecutionService } from 'src/app/core/services/test-execution.service';
import { TestExecution } from 'src/app/interfaces/test.execution';
import { Test } from 'src/app/interfaces/test';
import ConstRoutes from 'src/app/shared/contants/const-routes';
import { TestExecutionReviewComponent } from 'src/app/shared/components/test-execution-review-component/test-execution-review-component';
import { QuestionExecution } from 'src/app/interfaces/question-execution';

@Component({
  selector: 'app-perform-test-review',
  imports: [
      CommonModule, FormsModule, ButtonComponent, TestExecutionReviewComponent,  HomeNavigationComponent
  ],
  templateUrl: './perform-test-execution-review.component.html',
  styleUrl: './perform-test-execution-review.component.css'
})
export class PerformTestExecutionReviewComponent extends BasePage  {
  route: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router)

  testExecutionService: TestExecutionService = inject(TestExecutionService);

  id: number = null;
  name: string = null;
  description: string = '';
  subject: string = '';
  userName: string = '';
  result: number = 0;
  questions: QuestionExecution[] = []; 
  


  ngOnInit(): void {
    super.ngOnInit();
    let id =this.getIdToEdit();
    if (id) {
      this.loadTest(id);
    }
    
  }

  loadTest(id: number) {
    this.testExecutionService.findTest(id).subscribe({
        next: (response) => {
            if (isOkResponse(response)) {
              let test = loadResponseData(response);
              this.id = test.id;
              this.subject = test.subjectName,
              this.name = test.testName;
              this.description = test.testDescription;
              this.userName = test.userName;
              this.result = test.result;
              this.questions = test.questions;

            }
        }
      });
  }

  get isnew(): boolean {
    return this.id === undefined || this.id === null;
  } 


  onSave(test: Test) {


  }

  
}
