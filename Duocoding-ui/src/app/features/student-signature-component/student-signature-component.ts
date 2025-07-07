import { CommonModule } from '@angular/common';
import {Component, inject, OnInit} from '@angular/core';
import { BasePage } from '../base.page';
import {UserSubjectService} from "../../core/services/usersubject.service";

import {UserSubjectResponseDTO} from "../../interfaces/UserSubjectResponseDTO";
import {getUser} from "../../core/services/utils.service";
import {Router} from "@angular/router";
import {CardComponent} from "../../shared/components/card-component/card-component";


@Component({
  selector: 'app-student-signature',
  imports: [CommonModule, CardComponent],
  templateUrl: './student-signature-component.html',
  standalone: true,
  styleUrls: ['./student-signature-component.css']
})
export class StudentSignatureComponent extends BasePage implements OnInit {

  userSubjectService = inject(UserSubjectService);
  router = inject(Router);

  subjects: UserSubjectResponseDTO[] = [];

  ngOnInit(): void {
    super.ngOnInit();

    const loggedUser = getUser();
    const userId = loggedUser?.id;

    if (userId) {
      this.userSubjectService.getSubjectsByUser(userId).subscribe({
        next: data => this.subjects = data,
        error: err => console.error('Error al cargar asignaturas', err)
      });
    }
  }
}

