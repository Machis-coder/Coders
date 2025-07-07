import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { ButtonComponent } from '../button-component/button-component';
import { FormsModule } from '@angular/forms';
import { QuestionType } from '../../enums/question.types';
import { Question } from 'src/app/interfaces/question';
import { BasePage } from 'src/app/features/base.page';
import { Subject } from 'src/app/interfaces/subject';
import { validateQuestion } from 'src/app/utils/question.utils';
import { QuestionExecutionComponent } from './question-execution-component';

@Component({
  selector: 'app-test-execution',
  standalone: true,
  imports: [CommonModule, QuestionExecutionComponent, ButtonComponent, FormsModule ],
  templateUrl: './test-execution-component.html',
  styleUrl: './test-execution-component.css'
})
export class TestExecutionComponent extends BasePage implements OnInit, OnChanges {
  

  @Input() id: number;
  @Input() name: string;
  @Input() description: string;
  @Input() subject: string;
  
  @Input() questions: Question[];


  @Output() onsave = new EventEmitter<any>()
  @Output() onclean = new EventEmitter<any>()


  descriptionInternal: string;
  questionsInternal: Question[];
  answerInternal: string;
  index: number = 1;
  question: Question;

  
  ngOnInit(): void {
    this.questionsInternal = this.questions;
    this.question = this.questionsInternal[0];
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

  saveQuestion(question: Question) {
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

  goprevious(question: Question) {
    this.saveQuestion(question);
    this.navigateToQuestion(question.order -1);
  }

  gonext(question: Question) {
    this.saveQuestion(question);
    this.navigateToQuestion(question.order + 1);
  }

}
