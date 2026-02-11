import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScrimHistoryComponent } from './scrim-history.component';

describe('ScrimHistoryComponent', () => {
  let component: ScrimHistoryComponent;
  let fixture: ComponentFixture<ScrimHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ScrimHistoryComponent]
    });
    fixture = TestBed.createComponent(ScrimHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
