import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateScrimComponent } from './create-scrim.component';

describe('CreateScrimComponent', () => {
  let component: CreateScrimComponent;
  let fixture: ComponentFixture<CreateScrimComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateScrimComponent]
    });
    fixture = TestBed.createComponent(CreateScrimComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
