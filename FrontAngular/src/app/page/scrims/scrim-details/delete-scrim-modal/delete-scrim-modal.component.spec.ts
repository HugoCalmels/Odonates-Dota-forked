import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteScrimModalComponent } from './delete-scrim-modal.component';

describe('DeleteScrimModalComponent', () => {
  let component: DeleteScrimModalComponent;
  let fixture: ComponentFixture<DeleteScrimModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeleteScrimModalComponent]
    });
    fixture = TestBed.createComponent(DeleteScrimModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
