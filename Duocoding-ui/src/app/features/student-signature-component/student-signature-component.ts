import { CommonModule } from '@angular/common';
import {Component, inject, OnInit} from '@angular/core';
import { BasePage } from '../base.page';
import { HomeNavigationComponent } from 'src/app/shared/components/home-navigation-component/home-navigation-component';
import {UserSubjectService} from "../../core/services/usersubject.service";
import {LoginService} from "../../core/services/login.service";
import {UserSubjectResponseDTO} from "../../interfaces/UserSubjectResponseDTO";
import {getUser} from "../../core/services/utils.service";
import {Router} from "@angular/router";
import {CardComponent} from "../../shared/components/card-component/card-component";

@Component({
  selector: 'app-student-signature',
  imports: [CommonModule],
  templateUrl: './student-signature-component.html',
  standalone: true,
  styleUrls: ['./student-signature-component.css']
})
export class StudentSignatureComponent extends BasePage implements OnInit {

  userSubjectService = inject(UserSubjectService);
  router= inject(Router);

  subjects: UserSubjectResponseDTO[]=[];

  ngOnInit(): void {
    super.ngOnInit();
    const loggedUser = getUser();
    const userId = loggedUser?.id;

    this.userSubjectService.getSubjectsByUser(userId).subscribe({
      next:data => this.subjects=data,
      error: err => console.error (err)
    });
  }
  goToDetail(id: number) {
    this.router.navigate(['/detalle-materia', id]);
  }
}
