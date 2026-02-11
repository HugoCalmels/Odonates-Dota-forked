import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScrimListComponent } from './scrim-list.component';

describe('ScrimListComponent', () => {
  let component: ScrimListComponent;
  let fixture: ComponentFixture<ScrimListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ScrimListComponent]
    });
    fixture = TestBed.createComponent(ScrimListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
