import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Observable, of } from 'rxjs';

import { routes } from '../../../../app-routing.module';
import { FixNavigationTriggeredOutsideAngularZoneNgModule } from './fixing-navigation-trigger-outside-ngzone.module';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { By } from '@angular/platform-browser';

/**
 * NgModule as workaround for "Navigation triggered outside Angular zone" in tests
 *
 * https://github.com/angular/angular/issues/47236
 */

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let router: Router;
  let navigateSpy: jest.SpyInstance;

  const mockSessionInformation = {
    admin: true,
    id: 1,
    token: 'token',
    type: 'type',
    username: 'jodoe',
    firstName: 'John',
    lastName: 'Doe',
  };

  let mockAuthService: Partial<AuthService> = {
    login(LoginRequest: LoginRequest): Observable<SessionInformation> {
      return of(mockSessionInformation);
    },
  };

  let mockSessionService: Partial<SessionService> = {
    isLogged: true,
    sessionInformation: undefined,
    logIn(sessionInformation: SessionInformation): void {
      this.sessionInformation = sessionInformation;
      this.isLogged = true;
    },
    logOut(): void {
      this.sessionInformation = undefined;
      this.isLogged = false;
    },
    $isLogged(): Observable<boolean> {
      return of(true);
    },
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        FormBuilder,
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
      ],
      imports: [
        RouterTestingModule.withRoutes(routes),
        FixNavigationTriggeredOutsideAngularZoneNgModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    // Get injected router instance
    router = TestBed.inject(Router);
    navigateSpy = jest.spyOn(router, 'navigate');
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should require email and password', () => {
    const emailControl = component.form.get('email');
    const passwordControl = component.form.get('password');

    emailControl?.setValue('');
    passwordControl?.setValue('');

    expect(emailControl?.valid).toBeFalsy();
    expect(passwordControl?.valid).toBeFalsy();
  });

  it('should call AuthService.login on submit and navigate on success', async () => {
    const loginResponse: SessionInformation = {
      admin: true,
      id: 1,
      token: 'token',
      type: 'type',
      username: 'aldoe',
      firstName: 'Alice',
      lastName: 'Doe',
    };
    let email = component.form.controls['email'];
    let password = component.form.controls['password'];
    let submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;

    jest.spyOn(mockAuthService, 'login').mockReturnValue(of(loginResponse));
    jest.spyOn(mockSessionService, 'logIn').mockImplementationOnce(() => {
      mockSessionService.sessionInformation = loginResponse;
      mockSessionService.isLogged = true;
    });
    email?.setValue('test@example.com');
    password?.setValue('password');
    fixture.detectChanges();

    submitButton.click();

    expect(mockAuthService.login).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'password',
    });
    expect(mockSessionService.sessionInformation).toEqual(loginResponse);
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });
});
