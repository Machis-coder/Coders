import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignatureTestComponentComponent } from './signature-test-component.component';

describe('SignatureTestComponentComponent', () => {
  let component: SignatureTestComponentComponent;
  let fixture: ComponentFixture<SignatureTestComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SignatureTestComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SignatureTestComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
