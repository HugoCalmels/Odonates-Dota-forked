import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinTeamModalComponent } from './join-team-modal.component';

describe('JoinTeamModalComponent', () => {
  let component: JoinTeamModalComponent;
  let fixture: ComponentFixture<JoinTeamModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [JoinTeamModalComponent]
    });
    fixture = TestBed.createComponent(JoinTeamModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
