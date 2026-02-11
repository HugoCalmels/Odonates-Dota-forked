import { TestBed } from '@angular/core/testing';

import { LobbySauvageService } from './lobby-sauvage.service';

describe('LobbySauvageService', () => {
  let service: LobbySauvageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LobbySauvageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
