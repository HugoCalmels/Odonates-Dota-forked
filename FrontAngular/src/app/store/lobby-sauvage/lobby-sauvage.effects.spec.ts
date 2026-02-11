import { TestBed } from '@angular/core/testing';
import { provideMockActions } from '@ngrx/effects/testing';
import { Observable } from 'rxjs';

import { LobbySauvageEffects } from './lobby-sauvage.effects';

describe('LobbySauvageEffects', () => {
  let actions$: Observable<any>;
  let effects: LobbySauvageEffects;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        LobbySauvageEffects,
        provideMockActions(() => actions$)
      ]
    });

    effects = TestBed.inject(LobbySauvageEffects);
  });

  it('should be created', () => {
    expect(effects).toBeTruthy();
  });
});
