import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../interfaces/user';

import { isOkResponse, loadResponseData } from 'src/app/core/services/utils.service';
import { FormsModule } from '@angular/forms';
import { ButtonComponent } from 'src/app/shared/components/button-component/button-component';
import { Question } from 'src/app/interfaces/question';
import { CommonModule } from '@angular/common';
import { HomeNavigationComponent } from 'src/app/shared/components/home-navigation-component/home-navigation-component';
import { Response } from 'src/app/interfaces/response';
import { QuestionType } from 'src/app/shared/enums/question.types';
import { TestService } from 'src/app/core/services/test.service';
import { TestComponent } from 'src/app/shared/components/test-component/test-component';
import { Test } from 'src/app/interfaces/test';
import { Subject } from 'src/app/interfaces/subject';
import { SubjectService } from 'src/app/core/services/subject.service';
import {BasePage} from "../base.page";
import ConstRoutes from "../../shared/constants/const-routes";

@Component({
  selector: 'app-new-test',
  imports: [
      CommonModule, FormsModule, TestComponent,  HomeNavigationComponent
  ],
  templateUrl: './new-test.component.html',
  styleUrl: './new-test.component.css'
})
export class NewTestComponent extends BasePage  {
  route: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router)

  testService: TestService = inject(TestService);
  subjectService: SubjectService = inject(SubjectService);

  id: number = null;
  name: string = null;
  description: string = null;
  questions: Question[] = [];                                    
  subjects: Subject[] = [];
  subjectId: number;


  ngOnInit(): void {
    super.ngOnInit();
    
    this.loadSubjects();

    let id =this.getIdToEdit();
    if (id) {
      this.loadTest(id);
    }
    
  }

  loadSubjects() {

    this.subjectService.findSubjects().subscribe({
      next: (response) => {
        if (isOkResponse(response)) {
          this.subjects = loadResponseData(response);
          this.subjectId = this.subjects[1].id;
        }
      },
      error: (err) => {
        alert(err);
      }
    })
  }

  loadTest(id: number) {
    this.testService.findTest(id).subscribe({
        next: (response) => {
            if (isOkResponse(response)) {
              let test = loadResponseData(response);
              this.id = test.id;
              this.subjectId = test.subjectId,
              this.name = test.name;
              this.description = test.description;
              this.questions = test.questions;              
            }
        }
      });
  }

  addQuestion():void {
    let responses: Response[] = []
    this.questions.push({id: undefined, description:'', type:QuestionType.FREETEXT, responses, order: this.questions.length + 1, answer: ''});
  }

  get isnew(): boolean {
    return this.id === undefined || this.id === null;
  } 

  validateForm(): boolean {
    return true;
  }

  createOrUpdate(test: Test) {

  }


  onSave(test) {
    this.testService.save(test).subscribe({
      next: (response) => {
        if (isOkResponse(response)) {
          if (test.id) {
            this.navigateTo(ConstRoutes.PATH_TESTS);
          } else {
            this.navigateTo(ConstRoutes.PATH_HOME);
          }
        }
      },
      error: (err) => {
        alert(err);
      }
    })
  }

  onBack() {
  
  }

  
}
