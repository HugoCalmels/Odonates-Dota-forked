import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LobbySauvageDetailsComponent } from './lobby-sauvage-details.component';

describe('LobbySauvageDetailsComponent', () => {
  let component: LobbySauvageDetailsComponent;
  let fixture: ComponentFixture<LobbySauvageDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LobbySauvageDetailsComponent]
    });
    fixture = TestBed.createComponent(LobbySauvageDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
