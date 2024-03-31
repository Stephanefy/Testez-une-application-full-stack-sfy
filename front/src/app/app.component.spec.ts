import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { Observable, of } from 'rxjs';
import { SessionInformation } from './interfaces/sessionInformation.interface';

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;

  let mockSessionService: Partial<SessionService> = {
    isLogged: true,
    sessionInformation: {
      admin: true,
      id: 1,
      token: 'token',
      type: 'type',
      username: 'jodoe',
      firstName: 'John',
      lastName: 'Doe',
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
      imports: [RouterTestingModule, HttpClientModule, MatToolbarModule],
      declarations: [AppComponent],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should create the app', () => {
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should hide logout button if not logged in', async () => {
    jest.spyOn(mockSessionService, '$isLogged').mockReturnValue(of(false));
    fixture.detectChanges();

    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('.link[routerLink="login"]')).not.toBeNull();
    expect(
      compiled.querySelector('.link[routerLink="register"]')
    ).not.toBeNull();
    expect(compiled.querySelector('.link[routerLink="sessions"]')).toBeNull();
    expect(compiled.querySelector('.link[routerLink="me"]')).toBeNull();
    expect(compiled.querySelector('.logout-btn')).toBeNull();
  });

  it('should show auth buttons if logged in', async () => {
    jest.spyOn(mockSessionService, '$isLogged').mockReturnValue(of(true));
    fixture.detectChanges();

    const compiled = fixture.nativeElement;

    expect(
      compiled.querySelector('.link[routerLink="sessions"]')
    ).not.toBeNull();
    expect(compiled.querySelector('.link[routerLink="me"]')).not.toBeNull();
    expect(compiled.querySelector('.logout-btn')).not.toBeNull();

    expect(compiled.querySelector('.link[routerLink="login"]')).toBeNull();
    expect(compiled.querySelector('.link[routerLink="register"]')).toBeNull();
  });

  it('should logout when logout button is clicked', () => {
    jest.spyOn(mockSessionService, '$isLogged').mockReturnValue(of(true));
    mockSessionService.isLogged = true;
    fixture.detectChanges();

    const spy = jest.spyOn(mockSessionService, 'logOut');
    const compiled = fixture.nativeElement;

    compiled.querySelector('.logout-btn')?.click();

    expect(mockSessionService.isLogged).toBe(false);
    expect(spy).toHaveBeenCalled();
  });
});
