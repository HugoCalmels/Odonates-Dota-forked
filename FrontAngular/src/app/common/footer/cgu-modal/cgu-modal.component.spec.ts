import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CguModalComponent } from './cgu-modal.component';

describe('CguModalComponent', () => {
  let component: CguModalComponent;
  let fixture: ComponentFixture<CguModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CguModalComponent]
    });
    fixture = TestBed.createComponent(CguModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
