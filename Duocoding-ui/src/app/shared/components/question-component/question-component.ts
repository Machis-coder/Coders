import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { ResponseComponent } from '../response-component/response-component';
import { Response } from 'src/app/interfaces/response';
import { ButtonComponent } from '../button-component/button-component';
import { FormsModule } from '@angular/forms';
import { QuestionType } from '../../enums/question.types';
import { Question } from 'src/app/interfaces/question';
import {isUserInRole} from "../../../utils/user.utils";
import Role from "../../../roles";

@Component({
  selector: 'app-question',
  standalone: true,
  imports: [CommonModule, ResponseComponent, ButtonComponent, FormsModule ],
  templateUrl: './question-component.html',
  styleUrl: './question-component.css'
})

export class QuestionComponent implements OnInit, OnChanges {
  @Input() id: number;
  @Input() order: number;
  @Input() type: QuestionType;
  @Input() description: string;
  @Input() responses: Response[] = [];
  @Input() answer: string;
  @Input() editable: boolean = true;

  @Output() onsave = new EventEmitter<any>();
  @Output() ondelete = new EventEmitter<any>();
  @Output() onback = new EventEmitter<any>();
  @Output() onnext = new EventEmitter<any>();

  typeInternal: QuestionType;
  orderInternal: number;
  responsesInternal: Response[] = [];
  descriptionInternal: string = '';
  answerInternal: string = '';
  showResponses: boolean = false;
  canEdit: boolean = false; //

  ngOnInit(): void {
    this.orderInternal = this.order;
    this.descriptionInternal = this.description || '';
    this.typeInternal = this.type || QuestionType.MONOSELECTION;
    this.answerInternal = this.answer || '';
    this.responsesInternal = this.responses ? [...this.responses] : [];

    if (this.responsesInternal.length > 0) {
      const answers = this.answerInternal.split(',').filter(x => x !== '');
      this.responsesInternal.forEach(r => {
        r.checked = answers.includes(r.order.toString());
      });
    }

    this.showResponses = this.shouldShowResponses();


    this.canEdit = isUserInRole([Role.TEACHER, Role.ADMIN, Role.SUPER]);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }

  typechange($event: any) {
    this.typeInternal = $event.target.value as QuestionType;
    this.showResponses = this.shouldShowResponses();
    this.answerInternal = '';
  }

  textchange(newValue: string) {
    this.descriptionInternal = newValue;
  }

  answerchange(newValue: string) {
    this.answerInternal = newValue;
  }

  addResponse() {
    this.responsesInternal.push({
      order: this.responsesInternal.length + 1,
      description: '',
      checked: false
    });
  }

  saveResponse(response: { order: number, text: string }) {
    const target = this.responsesInternal.find(o => o.order === response.order);
    if (target) {
      target.description = response.text;
    }
  }

  removeResponse(response: Response) {
    this.responsesInternal = this.responsesInternal.filter(o => o.order !== response.order);
    this.responsesInternal.forEach((r, i) => r.order = i + 1);
    this.responseChangeSelection({ order: response.order, checked: false });
  }

  responseChangeSelection(selection: { order: number, checked: boolean }) {
    if (this.typeInternal === QuestionType.MONOSELECTION) {
      this.answerInternal = selection.order.toString();
      this.responsesInternal.forEach(r => r.checked = r.order === selection.order);
    } else if (this.typeInternal === QuestionType.MULTISELECTION) {
      const selected = this.answerInternal.split(',').filter(x => x !== '');

      if (selection.checked) {
        selected.push(selection.order.toString());
      } else {
        const idx = selected.indexOf(selection.order.toString());
        if (idx !== -1) selected.splice(idx, 1);
      }

      this.answerInternal = selected.join(',');
      const answers = selected;
      this.responsesInternal.forEach(r => r.checked = answers.includes(r.order.toString()));
    }
  }

  get types(): string[] {
    return Object.values(QuestionType);
  }

  get responsesList(): Response[] {
    return this.responsesInternal;
  }

  cleanResponses() {
    this.responsesInternal = this.responsesInternal.filter(r => r.description !== '');
  }

  ondeletepush() {
    this.typeInternal = QuestionType.MONOSELECTION;
    this.descriptionInternal = '';
    this.answerInternal = '';
    this.showResponses = this.shouldShowResponses();
    this.ondelete.emit({ order: this.orderInternal });
  }

  onbackpush() {
    this.cleanResponses();
    this.onback.emit({
      id: this.id,
      type: this.typeInternal,
      order: this.orderInternal,
      description: this.descriptionInternal,
      answer: this.answerInternal,
      responses: this.responsesInternal
    });
  }

  onnextpush() {
    this.cleanResponses();
    this.onnext.emit({
      id: this.id,
      type: this.typeInternal,
      order: this.orderInternal,
      description: this.descriptionInternal,
      answer: this.answerInternal,
      responses: this.responsesInternal
    });
  }

  shouldShowResponses(): boolean {
    return this.typeInternal === QuestionType.MONOSELECTION || this.typeInternal === QuestionType.MULTISELECTION;
  }

  trackByOrder(index: number, response: Response): number {
    return response.order;
  }
}