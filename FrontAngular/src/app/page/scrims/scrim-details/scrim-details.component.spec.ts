import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScrimDetailsComponent } from './scrim-details.component';

describe('ScrimDetailsComponent', () => {
  let component: ScrimDetailsComponent;
  let fixture: ComponentFixture<ScrimDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ScrimDetailsComponent]
    });
    fixture = TestBed.createComponent(ScrimDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
