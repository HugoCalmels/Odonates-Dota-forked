import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScrimsPageComponent } from './scrims-page.component';

describe('ScrimsPageComponent', () => {
  let component: ScrimsPageComponent;
  let fixture: ComponentFixture<ScrimsPageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ScrimsPageComponent]
    });
    fixture = TestBed.createComponent(ScrimsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
