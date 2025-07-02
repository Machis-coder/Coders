import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TestService } from 'src/app/core/services/test.service';
import { SubjectService } from 'src/app/core/services/subject.service';
import { Test } from 'src/app/interfaces/Test';
import { Subject } from 'src/app/interfaces/Subject';
import { FormsModule } from '@angular/forms';
import { ButtonComponent } from 'src/app/shared/components/button-component/button-component';

@Component({
  selector: 'app-test',
  imports: [FormsModule, ButtonComponent],
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent implements OnInit {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private testService = inject(TestService);
  private subjectService = inject(SubjectService);

  id: number | string = null;
  name: string = '';
  description: string = '';
  active: boolean = true;
  subjectId: number = null;

  subjects: Subject[] = [];

  ngOnInit() {
    this.loadSubjects();

    this.id = this.route.snapshot.params['id'];

    if (this.id && this.id !== 'new') {
      this.testService.findTest(+this.id).subscribe({
        next: response => {
          const test: Test = response.body;
          this.name = test.name;
          this.description = test.description;
          this.active = test.active;
          this.subjectId = test.subject?.id;
        },
        error: err => {
          console.error('Error loading test', err);
        }
      });
    }
  }

  loadSubjects() {
    this.subjectService.findSubjects().subscribe({
      next: response => {
        this.subjects = response.body || [];
      },
      error: err => {
        console.error('Error loading subjects', err);
      }
    });
  }

  validateForm(): boolean {
    return this.name.trim() !== '' && this.description.trim() !== '' && this.subjectId !== null;
  }

  createOrUpdate() {
    if (!this.validateForm()) {
      alert('Por favor complete todos los campos obligatorios');
      return;
    }

    const test: Test = {
      id: this.id === 'new' ? null : +this.id,
      name: this.name,
      description: this.description,
      active: this.active,
      subject: { id: this.subjectId } as Subject,
    };

    if (!test.id) {
      this.testService.save(test).subscribe({
        next: response => {
          this.router.navigate(['/tests']);
        },
        error: err => {
          console.error('Error saving test', err);
        }
      });
    } else {
      this.testService.update(test).subscribe({
        next: response => {
          this.router.navigate(['/tests']);
        },
        error: err => {
          console.error('Error updating test', err);
        }
      });
    }
  }

  onBack() {
    this.router.navigate(['/tests']);
  }
}
