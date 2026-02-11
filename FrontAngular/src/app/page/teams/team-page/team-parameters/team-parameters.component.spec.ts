import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamParametersComponent } from './team-parameters.component';

describe('TeamParametersComponent', () => {
  let component: TeamParametersComponent;
  let fixture: ComponentFixture<TeamParametersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TeamParametersComponent]
    });
    fixture = TestBed.createComponent(TeamParametersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
