import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableComponent } from 'src/app/shared/components/table-component/table-component';
import {
  isOkResponse,
  loadResponseData
} from "../../core/services/utils.service";
import { BasePage } from '../base.page';
import { Router } from '@angular/router';
import { HomeNavigationComponent } from 'src/app/shared/components/home-navigation-component/home-navigation-component';
import { ButtonComponent } from 'src/app/shared/components/button-component/button-component';
import { QuestionService } from 'src/app/core/services/question.service';
import { Question } from 'src/app/interfaces/question';
import ConstRoutes from 'src/app/shared/contants/const-routes';


@Component({
  selector: 'app-questions',
  standalone: true,
  imports: [CommonModule, TableComponent, HomeNavigationComponent, ButtonComponent],
  templateUrl: './questions.component.html',
  styleUrl: './questions.component.css'
})
export class QuestionsComponent extends BasePage {
  questionList: Question[] = [];

  router: Router = inject(Router);
  questionService: QuestionService = inject(QuestionService);


  ngOnInit() {
    super.ngOnInit();
    this.load();
  }

  load() {
    this.questionService.findQuestions().subscribe({
      next: (response) => {
        if (isOkResponse(response)) {
          this.questionList = loadResponseData(response);
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
    this.navigateTo(ConstRoutes.PATH_QUESTION + '/' + id);
  }

  gotoNew() {
    this.navigateTo(ConstRoutes.PATH_QUESTION);
  }

}

