import {CommonModule} from '@angular/common';
import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {ButtonComponent} from '../button-component/button-component';
import {FormsModule} from '@angular/forms';
import {QuestionType} from '../../enums/question.types';
import {Question} from 'src/app/interfaces/question';
import {QuestionComponent} from '../question-component/question-component';
import {BasePage} from 'src/app/features/base.page';
import {Subject} from 'src/app/interfaces/subject';
import {isUserInRole} from "../../../utils/user.utils";
import Role from "../../../roles";

@Component({
  selector: 'app-test',
  standalone: true,
  imports: [CommonModule, QuestionComponent, ButtonComponent, FormsModule ],
  templateUrl: './test-component.html',
  styleUrl: './test-component.css'
})
export class TestComponent implements OnInit, OnChanges {

  @Input() id: number;
  @Input() subjects: Subject[];
  @Input() subjectId: number;
  @Input() name: string;
  @Input() description: string;
  @Input() questions: Question[];

  @Output() onsave = new EventEmitter<any>();
  @Output() onclean = new EventEmitter<any>();

  subjectInternal: number;
  nameInternal: string;
  descriptionInternal: string;
  questionsInternal: Question[] = [];
  index: number = 0;
  question: Question;
  canEdit: boolean = false;

  ngOnInit(): void {
    this.subjectInternal = this.subjectId;
    this.nameInternal = this.name;
    this.descriptionInternal = this.description;
    this.questionsInternal = this.questions ? [...this.questions] : [];
    this.canEdit = isUserInRole([Role.TEACHER, Role.ADMIN, Role.SUPER]);

    if (this.questionsInternal.length === 0) {
      this.addQuestion();
    }

    this.index = 0;
    this.question = this.questionsInternal[this.index];

  }

  ngOnChanges(): void {

  }

  subjectchange($event: any) {
    this.subjectInternal = $event.target.value;
  }

  onsavepush() {
    const test = {
      id: this.id,
      subjectId: this.subjectInternal,
      name: this.nameInternal,
      description: this.descriptionInternal,
      questions: this.questionsInternal
    };

    alert('✅ Test guardado correctamente\n\n' + JSON.stringify(test, null, 2));
    this.onsave.emit(test);
  }

  oncleanpush() {
    this.ngOnInit();
  }

  saveQuestion(updatedQuestion: Question) {
    const index = this.questionsInternal.findIndex(q => q.order === updatedQuestion.order);
    if (index !== -1) {
      this.questionsInternal[index] = { ...updatedQuestion };
    }
  }

  removeQuestion(question: Question) {
    this.questionsInternal = this.questionsInternal.filter(q => q.order !== question.order);
    this.questionsInternal.forEach((q, i) => q.order = i + 1);
    this.index = Math.max(0, this.index - 1);
    this.question = this.questionsInternal[this.index];
  }

  goprevious(question: Question) {
    this.saveQuestion(question);
    if (this.index > 0) {
      this.index -= 1;
      this.question = this.questionsInternal[this.index];
    }
  }

  gonext(question: Question) {
    this.saveQuestion(question);

    if (this.index === this.questionsInternal.length - 1) {
      this.addQuestion();
    }

    this.index += 1;

    if (this.index >= this.questionsInternal.length) {
      this.index = this.questionsInternal.length - 1;
    }

    this.question = this.questionsInternal[this.index];
    console.log('Antes de guardar: index =', this.index, 'orden =', question.order);
  }

  addQuestion() {
    const newOrder = this.questionsInternal.length > 0
        ? Math.max(...this.questionsInternal.map(q => q.order)) + 1
        : 1;

    const newQuestion: Question = {
      id: null,
      type: QuestionType.MONOSELECTION,
      order: newOrder,
      description: '',
      responses: [],
      answer: ''
    };

    this.questionsInternal.push(newQuestion);
  }
}