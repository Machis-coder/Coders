import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TestexecutionService} from "../../core/services/testexecution.service";
import {TestExecutionGeneralDTO} from "../../interfaces/TestExecutionGeneralDTO";
import {BasePage} from "../base.page";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {HomeNavigationComponent} from "../../shared/components/home-navigation-component/home-navigation-component";

@Component({
  selector: 'app-test-review-student',
  imports: [
    DatePipe,
    FormsModule,
    HomeNavigationComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './test-review-student.component.html',
  styleUrl: './test-review-student.component.css'
})
export class TestReviewStudentComponent extends BasePage implements OnInit{
  router = inject(Router);
  route = inject(ActivatedRoute);
  testExecutionService = inject(TestexecutionService);

  testExecution?: TestExecutionGeneralDTO;
  loading = false;
  error = '';

  ngOnInit(): void {
    const id = this.getIdToEdit();
    if (id) {
      this.loadTestExecution(id);
    } else {
      this.error = 'No se especificó el ID del examen.';
    }
  }

  loadTestExecution(id: number) {
    this.loading = true;
    this.error = '';
    this.testExecutionService.getTestExecutionFULLById(id).subscribe({
      next: (data) => {
        this.testExecution = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el examen.';
        this.loading = false;
      }
    });
  }


}
