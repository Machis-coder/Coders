import { Component, inject, OnInit } from '@angular/core';
import { User } from '../../interfaces/user';
import { CommonModule } from '@angular/common';
import { TableComponent } from 'src/app/shared/components/table-component/table-component';
import { UserService } from 'src/app/core/services/user.service';
import {  
  isOkResponse,
  loadResponseData
} from "../../core/services/utils.service";
import { BasePage } from '../base.page';
import { Router } from '@angular/router';
import { HomeNavigationComponent } from 'src/app/shared/components/home-navigation-component/home-navigation-component';
import { SubjectResponseDTO } from 'src/app/interfaces/subjectResponseDTO';
import { SubjectService } from 'src/app/core/services/subjectservice';
import { FormControl, FormGroup, FormsModule, Validators } from '@angular/forms';
import { SubjectRequestDTO } from 'src/app/interfaces/subjectRequestDTO';
import { ReactiveFormsModule } from '@angular/forms';


@Component({
  selector: 'app-signatures',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule, TableComponent,FormsModule, HomeNavigationComponent],
  templateUrl: './signatures.component.html',
  styleUrl: './signatures.component.css'
})
export class SignaturesComponent extends BasePage {
    searchName = '';
  userList: User[] = [];
   subjectList: SubjectResponseDTO[] = [];
   subjectForm = new FormGroup({
    name: new FormControl('', Validators.required),
    description: new FormControl(''),

     
  });
  router: Router = inject(Router);
  userService: UserService = inject(UserService);
  subjectService: SubjectService = inject(SubjectService);

  ngOnInit() {
      super.ngOnInit();
      this.loadUsers();
      this.loadSubjects();
  }
  
  loadUsers() {
    this.userService.findUsers().subscribe({
      next: (response) => {
        if (isOkResponse(response)) {
          this.userList = loadResponseData(response);
        } else {
          //this.error = loadResponseError(response);
        }
      },
      error: (err) =>{
        console.log(err);
      }
      
    });      
  } 

  gotoEdit(id) {
    this.router.navigate(['/user/' + id]);
  }

  gotoNew() {
    this.router.navigate(['/user']);
  }




  //asignautras
  
 loadSubjects() {
  this.subjectService.findSubjects().subscribe({
    next: (response) => {
      if (isOkResponse(response)) {
        this.subjectList = loadResponseData(response);
        console.log('Subjects cargados:', this.subjectList);
      } else {
        console.error('Respuesta no OK:', response);
      }
    },
    error: (err) => {
      console.error('Error al cargar asignaturas:', err);
    }
  });
  console.log("hola");
}

 selectedSubjectId: number | null = null;
  // Carga datos al formulario para editar
  editSubject(subject: SubjectResponseDTO) {
    this.selectedSubjectId = subject.id;
    this.subjectForm.setValue({
      name: subject.name,
      description: subject.description
    });
  }

  // Limpiar formulario para crear nuevo
  newSubject() {
    this.selectedSubjectId = null;
    this.subjectForm.reset();
  }

saveSubject() {
  if (this.subjectForm.invalid) return;

  const dto = this.subjectForm.value as SubjectRequestDTO;

  if (this.selectedSubjectId) {
    this.subjectService.update(this.selectedSubjectId, dto).subscribe({
      next: () => {
        this.loadSubjects();
        this.newSubject();
      },
      error: err => console.error('Error actualizando', err)
    });
  } else {
    this.subjectService.save(dto).subscribe({
      next: () => {
        this.loadSubjects();
        this.newSubject();
      },
      error: err => console.error('Error creando', err)
    });
  }
}

  // Borrar asignatura
  deleteSubject(id: number) {
    this.subjectService.delete(id).subscribe({
      next: () => this.loadSubjects(),
      error: err => console.error('Error borrando', err)
    });
  }

  // Activar asignatura
  activateSubject(id: number) {
    this.subjectService.activate(id).subscribe({
      next: () => this.loadSubjects(),
      error: err => console.error('Error activando', err)
    });
  }
 
  
}

