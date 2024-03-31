import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { SessionService } from '../services/session.service';

import { routes } from '../features/auth/auth-routing.module';

describe('AuthGuard', () => {
  let authGuard: AuthGuard;
  let sessionServiceMock: any;
  let router: Router;

  beforeEach(() => {
    sessionServiceMock = { isLogged: false }; 

    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes(routes)], // Setup routing
      providers: [
        AuthGuard,
        { provide: SessionService, useValue: sessionServiceMock }
      ],
    });

    authGuard = TestBed.inject(AuthGuard);
    router = TestBed.inject(Router);
  });

  it('should navigate to the login page if session is not logged and return false', () => {
    const spy = jest.spyOn(router, 'navigate');
    const result = authGuard.canActivate();
    expect(spy).toHaveBeenCalledWith(['login']);
    expect(result).toBe(false);
  });

  it('should return true if session is logged', () => {
    sessionServiceMock.isLogged = true;
    const result = authGuard.canActivate();
    expect(result).toBe(true);
  });
});
