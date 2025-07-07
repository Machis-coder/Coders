import {Component, inject, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {CommonModule, DatePipe} from '@angular/common';
import {TestExecutionGeneralDTO} from "../../interfaces/TestExecutionGeneralDTO";
import {TestexecutionService} from "../../core/services/testexecution.service";
import {BasePage} from "../base.page";
import {getUser} from "../../core/services/utils.service";
import {TestExecutionResponseDTO} from "../../interfaces/TestExecutionResponseDTO";
import {HomeNavigationComponent} from "../../shared/components/home-navigation-component/home-navigation-component";
import {NotesFromTeacherRequestDTO} from "../../interfaces/NotesFromTeacherRequestDTO";
import {Observable} from "rxjs";
import {FormsModule} from "@angular/forms";


@Component({
    selector: 'app-test-review-teacher',
    templateUrl: './test-review-teacher.component.html',
    imports: [
        HomeNavigationComponent,
        DatePipe,
        CommonModule,
        FormsModule
    ],
    styleUrls: ['./test-review-teacher.component.css']
})
export class TestReviewTeacherComponent implements OnInit {

    router = inject(Router);
    route = inject(ActivatedRoute);
    testExecutionService = inject(TestexecutionService);

    testExecution?: TestExecutionGeneralDTO;
    loading = false;
    error = '';

    ngOnInit(): void {
        const id = Number(this.route.snapshot.paramMap.get('id'));
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
    saveNotes(resp: any) {
        if (!this.testExecution) {
            return;
        }

        const dto = {
            testExecutionResponseNotes: resp.notes || '',
            testExecutionNotes: this.testExecution.notes || '',
            testExecutionId: this.testExecution.id,
            testExecutionResponseId: resp.id
        };

        this.testExecutionService.saveTestExecutionNotes(dto).subscribe({
            next: () => {
                alert('Notas guardadas correctamente');
            },
            error: (err) => {
                alert('Error guardando las notas: ' + err.message);
            }
        });
    }

    saveGeneralNotes(): void {
        const dto: NotesFromTeacherRequestDTO = {
            testExecutionNotes: this.testExecution.notes,
            testExecutionResponseNotes: '', // Solo para notas generales
            testExecutionId: this.testExecution.id,
            testExecutionResponseId: 0 // o null si tu backend lo acepta
        };

        this.testExecutionService.saveTestExecutionNotes(dto).subscribe({
            next: () => {
                alert('Notas generales guardadas correctamente');
            },
            error: (err) => {
                alert('Error guardando las notas generales: ' + err.message);
            }
        });
    }

}
