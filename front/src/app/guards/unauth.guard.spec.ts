import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { UnauthGuard } from './unauth.guard';
import { SessionService } from '../services/session.service';

import { routes } from '../app-routing.module';

type PartialSessionService = Partial<SessionService>;


describe('UnauthGuard', () => {
  let unauthGuard: UnauthGuard;
  let sessionServiceMock: PartialSessionService;
  let router: Router;

  beforeEach(() => {
    sessionServiceMock = { isLogged: true }; 

    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes(routes)], // Setup routing
      providers: [
        UnauthGuard,
        { provide: SessionService, useValue: sessionServiceMock }
      ],
    });

    unauthGuard = TestBed.inject(UnauthGuard);
    router = TestBed.inject(Router);
  });

  it('should navigate to the sessions (yoga sessions) page if session is logged and return false', () => {
    const spy = jest.spyOn(router, 'navigate');
    const result = unauthGuard.canActivate();
    expect(spy).toHaveBeenCalledWith(['rentals']);
    expect(result).toBe(false);
  });

  it('should return false if session is not logged', () => {
    sessionServiceMock.isLogged = false;
    const result = unauthGuard.canActivate();
    expect(result).toBe(true);
  });
});
