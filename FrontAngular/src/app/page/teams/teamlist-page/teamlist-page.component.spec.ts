import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamlistPageComponent } from './teamlist-page.component';

describe('TeamlistPageComponent', () => {
  let component: TeamlistPageComponent;
  let fixture: ComponentFixture<TeamlistPageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TeamlistPageComponent]
    });
    fixture = TestBed.createComponent(TeamlistPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
