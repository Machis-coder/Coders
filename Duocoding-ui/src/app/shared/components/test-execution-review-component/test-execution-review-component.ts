import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { ButtonComponent } from '../button-component/button-component';
import { FormsModule } from '@angular/forms';
import { QuestionType } from '../../enums/question.types';
import { Question } from 'src/app/interfaces/question';
import { BasePage } from 'src/app/features/base.page';
import { Subject } from 'src/app/interfaces/subject';
import { validateQuestion } from 'src/app/utils/question.utils';
import { QuestionExecutionReviewComponent } from './question-execution-review-component';
import { QuestionExecution } from 'src/app/interfaces/question-execution';

@Component({
  selector: 'app-test-execution-review',
  standalone: true,
  imports: [CommonModule, QuestionExecutionReviewComponent, ButtonComponent, FormsModule ],
  templateUrl: './test-execution-review-component.html',
  styleUrl: './test-execution-review-component.css'
})
export class TestExecutionReviewComponent extends BasePage implements OnInit, OnChanges {
  

  @Input() id: number;
  @Input() name: string;
  @Input() description: string;
  @Input() subject: string;
  @Input() answer: string;
  @Input() username: string;
  @Input() result: number;

  
  @Input() questions: QuestionExecution[];


  @Output() onsave = new EventEmitter<any>()
  @Output() onclean = new EventEmitter<any>()


  nameInternal: string;
  descriptionInternal: string;
  questionsInternal: QuestionExecution[];
  answerInternal: string;
  index: number = 0;
  question: QuestionExecution;
  userAnswer: string

  
  ngOnInit(): void {
    this.nameInternal = this.name;
    this.questionsInternal = this.questions? this.questions:[];
    this.navigateToQuestion(1);
  }
  
  navigateToQuestion(order: number): void {
    if (order < 1) {
      order = this.questionsInternal.length;
    }
    if (order > this.questionsInternal.length) {
      order = 1;
    }
    this.index = order - 1;
    this.question = this.questionsInternal[this.index];    
  }


  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }  
  
  cleanQuestions() {
    this.questionsInternal = this.questionsInternal.filter(r => r.description !== '');
  }

  validateQuestions() {
    return this.questionsInternal.filter(q => validateQuestion(q)).length == this.questionsInternal.length;
  }


  onsavepush() {
      let test = {
        id: this.id,
        name: this.name,
        description: this.descriptionInternal,
        questions: this.questionsInternal
      };
      this.onsave.emit(test);    
  }

  oncleanpush() {
    this.ngOnInit();
  }

  saveQuestion(question: QuestionExecution) {
    this.questionsInternal.map(q => {
      if (q.order === question.order) {
        q.id = question.id;
        q.type = question.type;
        q.description = question.description;
        q.responses = question.responses;
        q.answer = question.answer;
      }
      return q;
    });    
  }

  goprevious(question: QuestionExecution) {
    //this.saveQuestion(question);
    this.navigateToQuestion(question.order -1);
  }

  gonext(question: QuestionExecution) {
    //this.saveQuestion(question);
    this.navigateToQuestion(question.order + 1);
  }

}
