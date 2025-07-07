import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/core/services/user.service';
import { User } from 'src/app/interfaces/user';
import { ButtonComponent } from "../../shared/components/button-component/button-component";
import { FormsModule } from '@angular/forms';
import {CommonModule} from "@angular/common";
import Role from "../../roles";
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [CommonModule, ButtonComponent, FormsModule]
})
export class RegisterComponent {

  user: User = {
    id: null,
    name: '',
    surname: '',
    username: '',
    email: '',
    password: '',
    birthday: null,
    dni: '',
    role: null,
    repassword: ''
  };

  errorMessage: string = '';
  successMessage: string = '';

  constructor(private userService: UserService, private router: Router) { }

  onRegister() {
    this.errorMessage = '';
    this.successMessage = '';
    this.user.role = Role.PUPIL;
    this.userService.save(this.user).subscribe({
      next: response => {
        if (response.status === 201) {
          this.successMessage = 'Usuario registrado correctamente.';
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        } else {
          this.errorMessage = 'Error en el registro.';
        }
      },
      error: err => {
        if (err.status === 400) {
          this.errorMessage = 'Datos inválidos o usuario ya existe.';
        } else {
          this.errorMessage = 'Error en el servidor, intenta más tarde.';
        }
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
