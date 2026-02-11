import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LobbySauvageComponent } from './lobby-sauvage.component';

describe('LobbyComponent', () => {
  let component: LobbySauvageComponent;
  let fixture: ComponentFixture<LobbySauvageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LobbySauvageComponent]
    });
    fixture = TestBed.createComponent(LobbySauvageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
