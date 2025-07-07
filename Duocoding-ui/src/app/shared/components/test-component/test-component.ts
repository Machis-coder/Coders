import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { ButtonComponent } from '../button-component/button-component';
import { FormsModule } from '@angular/forms';
import { QuestionType } from '../../enums/question.types';
import { Question } from 'src/app/interfaces/question';
import { QuestionComponent } from '../question-component/question-component';
import { BasePage } from 'src/app/features/base.page';
import { Subject } from 'src/app/interfaces/subject';
import { validateQuestion } from 'src/app/utils/question.utils';

@Component({
  selector: 'app-test',
  standalone: true,
  imports: [CommonModule, QuestionComponent, ButtonComponent, FormsModule ],
  templateUrl: './test-component.html',
  styleUrl: './test-component.css'
})
export class TestComponent extends BasePage implements OnInit, OnChanges {
  

  @Input() id: number;
  @Input() subjects: Subject[];
  @Input() subjectId: number;
  @Input() name: string;
  @Input() description: string;
  
  @Input() questions: Question[];


  @Output() onsave = new EventEmitter<any>()
  @Output() onclean = new EventEmitter<any>()


  subjectInternal: number;
  nameInternal: string;
  descriptionInternal: string;
  questionsInternal: Question[];
  answerInternal: string;
  index: number = 0;
  question: Question;

  
  ngOnInit(): void {    
    this.subjectInternal = this.subjectId;
    this.nameInternal = this.name;
    this.descriptionInternal = this.description;
    this.questionsInternal = this.questions? this.questions:[];
    this.navigateToQuestion(1);
  }
  
  navigateToQuestion(order: number): void {
    if (order < 1) {
      order = 1;
    }
    this.index = order - 1;
    if (this.questionsInternal.length == 0 || this.index == this.questionsInternal.length) {
      this.addQuestion();      
    }    
    this.question = this.questionsInternal[this.index];    
  }


  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }


  subjectchange($event) {
    this.subjectInternal = $event.target.value;    
  }
  
  cleanQuestions() {
    this.questionsInternal = this.questionsInternal.filter(r => r.description !== '');
  }

  validateQuestions() {
    return this.questionsInternal.filter(q => validateQuestion(q)).length == this.questionsInternal.length;
  }

  validateTest() {
    return this.subjectInternal !== undefined && this.nameInternal !== '' && this.descriptionInternal !== '' 
    && this.questionsInternal.length > 0 && this.validateQuestions();
  }


  onsavepush() {    

    if (this.validateTest()) {
      let test = {
        id: this.id,
        subjectId: this.subjectInternal,
        name: this.nameInternal,
        description: this.descriptionInternal,
        questions: this.questionsInternal
      };

      alert(JSON.stringify(test));
      this.onsave.emit(test);
    } else {
      alert("El test no esta configurado correctamente.");
    }
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

  removeQuestion(question: Question) {
    this.questionsInternal = this.questionsInternal.filter(o => o.order !== question.order);
    for (let i = 0; i < this.questionsInternal.length; i++) {
      this.questionsInternal[i].order = i+1;
    }   
    this.navigateToQuestion(question.order - 1);
  }



  goprevious(question: Question) {
    this.saveQuestion(question);
    this.navigateToQuestion(question.order -1);
  }

  gonext(question: Question) {
    this.saveQuestion(question);
    this.navigateToQuestion(question.order + 1);
  }

  


  addQuestion() {
    if (!this.validateQuestions()) {
      alert("Rellene primero las preguntas incompletas");
    } else {
      this.questionsInternal.push({id: null, type: QuestionType.FREETEXT,  order:this.questionsInternal.length +1, description:'', responses: [], answer: ''});
    }
  } 

}
