import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { ButtonComponent } from '../button-component/button-component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-response-execution-review',
  standalone: true,
  imports: [CommonModule, ButtonComponent, FormsModule],
  templateUrl: './response-execution-review-component.html',
  styleUrl: './response-execution-review-component.css'
})
export class ResponseExecutionReviewComponent implements OnInit, OnChanges {
  
  @Input() text: string;
  @Input() order: number;
  @Input() type: string;
  @Input() answer: string;
  @Input() checked: boolean;
  @Input() correct: boolean;
  
  @Output() remove = new EventEmitter<any>();

  @Output() changeSelection = new EventEmitter<any>();
  @Output() changeText = new EventEmitter<any>();

  orderInternal: number
  textInternal: string;
  typeInternal: string;
  answerInternal:string;
  checkedInternal: boolean;
  isMonoSelection:boolean = false;
  isMultiSelection:boolean = false;

  ngOnInit(): void {
    this.orderInternal = this.order;
    this.textInternal = this.text;
    this.typeInternal = this.type;
    this.answerInternal = this.answer;
    this.checkedInternal = this.checked;
    this.isMonoSelection = this.type === 'MONOSELECTION';
    this.isMultiSelection = this.type === 'MULTISELECTION';
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }

  onRemove() {
    this.remove.emit({order: this.orderInternal, text: this.textInternal});
  }

  onSelectionChange($event) {
    this.checkedInternal = $event.target.checked;
    this.changeSelection.emit({order: this.orderInternal, checked: this.checkedInternal}); 
  }

  onChangeText() {
    this.changeText.emit({order: this.orderInternal, text: this.textInternal});
  }

}

export enum QuestionType {
  FREETEXT,
  MONOSELECTION,
  MULTISELECTION,
  GAP,
  CODE
}
