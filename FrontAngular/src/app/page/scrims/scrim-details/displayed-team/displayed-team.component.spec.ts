import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayedTeamComponent } from './displayed-team.component';

describe('DisplayedTeamComponent', () => {
  let component: DisplayedTeamComponent;
  let fixture: ComponentFixture<DisplayedTeamComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DisplayedTeamComponent]
    });
    fixture = TestBed.createComponent(DisplayedTeamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
