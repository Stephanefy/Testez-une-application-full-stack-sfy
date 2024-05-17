import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Observable, of, throwError } from 'rxjs';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { Router } from '@angular/router';
import { FixNavigationTriggeredOutsideAngularZoneNgModule } from '../login/fixing-navigation-trigger-outside-ngzone.module';
import { By } from '@angular/platform-browser';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let mockAuthService: Partial<AuthService> = {
    register(registerRequest: RegisterRequest): Observable<void>{
      return of(undefined);
    }
  };
  let router: Router;
  let navigateSpy: jest.SpyInstance;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        FixNavigationTriggeredOutsideAngularZoneNgModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    navigateSpy = jest.spyOn(router, 'navigate');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return false when form is empty', () => {
    expect(component.form.valid).toBeFalsy();
  });

  it('should navigate to login page when registration is valid', async () => {
    jest.spyOn(mockAuthService, 'register').mockReturnValue(of(undefined));
    component.form.controls['email'].setValue('test@example.com');
    component.form.controls['firstName'].setValue('John');
    component.form.controls['lastName'].setValue('Doe');
    component.form.controls['password'].setValue('password123');

    let submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;
    fixture.detectChanges();

    submitButton.click();


    expect(mockAuthService.register).toHaveBeenCalledWith(component.form.value);
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);

  })

  it('sets onError to true on register error', () => {
    jest.spyOn(mockAuthService, 'register').mockReturnValue(throwError(() => new Error('error on registration')));

    component.submit();

    expect(component.onError).toBeTruthy();
  });

});
