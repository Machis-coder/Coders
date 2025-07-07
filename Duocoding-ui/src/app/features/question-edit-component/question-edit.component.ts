import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HomeNavigationComponent } from 'src/app/shared/components/home-navigation-component/home-navigation-component';
import { QuestionComponent } from 'src/app/shared/components/question-component/question-component';
import { Response } from 'src/app/interfaces/response';
import { BasePage } from '../base.page';
import { QuestionService } from 'src/app/core/services/question.service';
import { Question } from 'src/app/interfaces/question';
import { QuestionType } from 'src/app/shared/enums/question.types';
import { isOkResponse, loadResponseData } from 'src/app/core/services/utils.service';


@Component({
  selector: 'app-question-test',
  standalone: true,
  imports: [CommonModule, HomeNavigationComponent, QuestionComponent],
  templateUrl: './question-edit.component.html',
  styleUrl: './question-edit.component.css'
})
export class QuestionEditComponent extends BasePage {
  
  router: Router = inject(Router);
  questionService: QuestionService = inject(QuestionService);

  responses: Response[] = [];
  id: number = undefined;
  description: string = '';
  type: QuestionType = QuestionType.FREETEXT;
  answer: string = '';
  

  ngOnInit() {
      super.ngOnInit()
      let questionId = this.getIdToEdit();
          if (questionId) {
              this.questionService.findQuestion(questionId).subscribe({
                next: (response) => {
                    const question: Question = loadResponseData(response);
                    this.id = question.id;
                    this.description = question.description;
                    this.type = QuestionType[question.type.toString()];
                    this.answer = question.answer;
                    
                    let answers: string[] = this.answer.split(",").filter(x => x !== '');
                    this.responses = question.responses;
                    this.responses.forEach(r => {
                      r.checked = answers.indexOf(r.order.toString()) > -1;
                    });
                }, 
                error: (err) => { 
                  console.log(err);
                }
              })
          }
  } 
  
  get responseList(): Response[] {
    return this.responses;
  }

  save(question:Question) {
    if (!question.id) {
      this.questionService.save(question).subscribe({
        next: (response) => {
          if (isOkResponse(response)) {
            // Nothing
          } else {
            alert("An error occurred saving the question");
          }
          }, 
        error: (err) => {
            alert("error");
        }
      })
    } else {
      this.questionService.update(question).subscribe({
        next: (response) => {
          if (isOkResponse(response)) {
            // Nothing
          } else {
            alert("An error occurred updating the question");
          }
          }, 
        error: (err) => {
            alert("error");
        }
      })
    }
 
  }
}
