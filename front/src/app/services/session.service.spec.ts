import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { mockUserSessionInfo } from '../shared/tests/mocks';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should start with isLogged as false and no sessionInformation', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should update isLogged to true and set sessionInformation on logIn', (done) => {
  

    // Subscribe to the $isLogged Observable to test its emission
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
      expect(service.isLogged).toBe(true);
      expect(service.sessionInformation).toEqual(mockUserSessionInfo);
      done();
    });

    service.logIn(mockUserSessionInfo);
  });

  it('should reset isLogged to false and clear sessionInformation on logOut', done => {
    // First, log in the user
    service.logIn(mockUserSessionInfo);
  
    // Subscribe to the $isLogged Observable to test its emission post-logout
    service.$isLogged().subscribe(isLogged => {
      if (!isLogged) {
        expect(isLogged).toBe(false);
        expect(service.isLogged).toBe(false);
        expect(service.sessionInformation).toBeUndefined();
        done();
      }
    });
  
    service.logOut();
  });
  
});
