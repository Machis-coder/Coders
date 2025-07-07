import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TestService } from 'src/app/core/services/test.service';
import { TestexecutionService } from 'src/app/core/services/testexecution.service';
import {TestResponseDTO} from "../../interfaces/TestResponseDTO";
import {TestExecutionGeneralDTO} from "../../interfaces/TestExecutionGeneralDTO";
import {getUser} from "../../core/services/utils.service";
import {CommonModule} from "@angular/common";
import {BasePage} from "../base.page";
import {HomeNavigationComponent} from "../../shared/components/home-navigation-component/home-navigation-component";
import {userId} from "../../utils/user.utils";
import ConstRoutes from "../../shared/contants/const-routes";


@Component({
  selector: 'app-signature-test',
  standalone: true,
  templateUrl: './signature-test.component.html',
  styleUrls: ['./signature-test.component.css'],
  imports: [CommonModule, HomeNavigationComponent],
})
export class SignatureTestComponent extends BasePage implements OnInit {
  route = inject(ActivatedRoute);
  testService = inject(TestService);
  testExecutionService = inject(TestexecutionService);

  subjectId: number | null = null;
  currentUserId: number | null = null;

  availableTests: TestResponseDTO[] = [];
  testExecutions: TestExecutionGeneralDTO[] = [];

  loadingTests = false;
  errorTests: string | null = null;

  loadingExecutions = false;
  errorExecutions: string | null = null;

  testsWereLoaded = false;

  ngOnInit(): void {
    this.currentUserId = userId();

    this.route.paramMap.subscribe(params => {
      const id = params.get('subjectId');
      this.subjectId = id ? +id : null;

      if (this.subjectId != null && this.currentUserId != null) {
        this.testsWereLoaded = true;
        this.loadAvailableTests(this.subjectId);
        this.loadTestExecutions(this.currentUserId, this.subjectId);
      }
    });
  }

  loadAvailableTests(subjectId: number) {
    this.loadingTests = true;
    console.log('Cargando tests para subjectId:', subjectId); // <-- AÑADIR

    this.testService.getAvailableTestsBySubject(subjectId).subscribe({
      next: data => {
        console.log('Tests recibidos:', data); // <-- AÑADIR
        this.availableTests = data;
        this.loadingTests = false;
      },
      error: err => {
        console.error('Error al cargar tests:', err); // <-- AÑADIR
        this.errorTests = 'Error al cargar tests disponibles';
        this.loadingTests = false;
      }
    });
  }

  loadTestExecutions(userId: number, subjectId: number) {
    this.loadingExecutions = true;
    this.testExecutionService.getActiveTestExecutionsByUserAndSubject(userId, subjectId).subscribe({
      next: data => {
        this.testExecutions = data;
        this.loadingExecutions = false;
      },
      error: err => {
        this.errorExecutions = 'Error al cargar ejecuciones';
        this.loadingExecutions = false;
      }
    });
  }

  onAvailableTestClick(test: TestResponseDTO) {
    console.log('Test clicado:', test.id);
    this.navigateTo(ConstRoutes.PATH_PERFORM_TEST + '/' + test.id)
  }
  onExecutionClick(execution: TestExecutionGeneralDTO) {
    this.navigateTo(ConstRoutes.PATH_TEST_REVIEW + '/' + execution.id);
  }
}
