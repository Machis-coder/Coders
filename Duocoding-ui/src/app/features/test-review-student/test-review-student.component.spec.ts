import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestReviewStudentComponent } from './test-review-student.component';

describe('TestReviewStudentComponent', () => {
  let component: TestReviewStudentComponent;
  let fixture: ComponentFixture<TestReviewStudentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestReviewStudentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TestReviewStudentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
