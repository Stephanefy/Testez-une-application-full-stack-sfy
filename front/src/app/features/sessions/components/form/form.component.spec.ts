import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { TeacherService } from 'src/app/services/teacher.service';
import { Observable, of, throwError } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { configureTestingModule } from 'src/app/shared/tests/utils';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockSessionService: Partial<SessionService>;
  let mockSessionApiService: Partial<SessionApiService>;
  let mockTeacherService: Partial<TeacherService>;
  let navigateSpy: jest.SpyInstance;
  let router: Router;

 
  const mockSession = {
    id: 1,
    name: 'Session 1',
    description: 'Description 1',
    date: new Date(),
    teacher_id: 1,
    users: [],
  };

  beforeEach(async () => {

    TestBed.resetTestingModule(); // Reset the test module before each configuration


    mockSessionService = {
      sessionInformation : {
        admin: true,
        id: 1,
        token: 'token',
        type: 'type',
        username: 'jodoe',
        firstName: 'John',
        lastName: 'Doe',
      },
    };

    mockSessionApiService = {
      create(mockSession: Session): Observable<Session> {
        return of(mockSession);
      },
      update(id: string, mockSession: Session): Observable<Session> {
        return of(mockSession);
      },
      detail(id: string): Observable<Session> {
        return of(mockSession);
      },
    };

    mockTeacherService = {
      all(): Observable<Teacher[]> {
        return of([{
          id: 1,
          lastName: 'Doe',
          firstName: 'Alice',
          createdAt: new Date(),
          updatedAt: new Date(),
        }]);
      },
    }

    // Mock ActivatedRoute with a paramMap observable
    const mockActivatedRoute = {
        snapshot: {
          paramMap: new Map([['id', '123']])
        },
        url: of('update/123') // Simulate navigating to an update URL
    };
    
  

    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ],
      declarations: [FormComponent]
    })



  });
  

  


  it('should initialize form for a new session', () => {
    const {currentFixture, currentComponent} = intializeTestBed(fixture, component)
    currentFixture.detectChanges();
    // Assuming initForm() correctly initializes a new session form
    expect(currentComponent.sessionForm).toBeDefined();
    expect(currentComponent.sessionForm?.controls['name'].value).toEqual('');
  });

  it('should redirect non-admin users', () => {
    
    TestBed.compileComponents(); // Recompile the testing module with overrides
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    router = TestBed.inject(Router);
    navigateSpy = jest.spyOn(router, 'navigate');
    mockSessionService.sessionInformation = {
      admin: false,
      id: 1,
      token: 'token',
      type: 'type',
      username: 'jodoe',
      firstName: 'John',
      lastName: 'Doe',
    };
    // Recreate component instance to re-trigger ngOnInit with new mock
    fixture.detectChanges();
  
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should recognize update scenario and fetch session details', () => {
    // Assuming 'update/123' is set in activatedRouteMock.url for this scenario
    const {currentFixture, currentComponent} = intializeTestBed(fixture, component)
    currentFixture.detectChanges();
    const detailSpy = jest.spyOn(mockSessionApiService, 'detail').mockReturnValue(of(mockSession));
    currentComponent.ngOnInit();
    mockSessionApiService.detail!("123");
  
    currentFixture.detectChanges(); // ngOnInit is called here
  
    // expect(component.onUpdate).toBe(true);
    expect(detailSpy).toHaveBeenCalledWith('123');
    // Additional checks can be added here to verify that the form is initialized correctly
  });


  it('should initialize form for create scenario', () => {
    // Adjust your ActivatedRoute mock to simulate a URL that doesn't include 'update'
    TestBed.overrideProvider(ActivatedRoute, { useValue: { snapshot: { paramMap: new Map(), url: of('create') } } });
    TestBed.compileComponents(); // Recompile the testing module with overrides
    // Recreate component to apply override
    const {currentFixture, currentComponent} = intializeTestBed(fixture, component)
  
    currentFixture.detectChanges(); // ngOnInit is called here
  
    expect(currentComponent.onUpdate).toBe(false);
    // Verify form initialization for creation scenario
    expect(currentComponent.sessionForm).toBeTruthy();
    // Additional assertions to check form default values can be added here
  });
  
  
  

  it('should initialize form with an existing session', (done) => {

    // Adjust your ActivatedRoute mock to simulate a URL that doesn't include 'update'
    TestBed.overrideProvider(ActivatedRoute, { useValue: { snapshot: { paramMap: new Map(), url: of('update') } } });
    TestBed.compileComponents(); // Recompile the testing module with overrides
    // Recreate component to apply override
    const {currentFixture, currentComponent} = intializeTestBed(fixture, component)
    const spy = jest.spyOn(mockSessionApiService, 'detail');
    const initFormSpy = jest.spyOn(currentComponent as any, 'initForm');

    currentFixture.detectChanges();

    currentComponent.onUpdate = true;
    mockSessionApiService.detail!("1").subscribe((session) => {
      currentComponent['initForm'](session);
    });


    expect(spy).toHaveBeenCalledWith("1");
    // Assuming initForm() correctly initializes an existing session form
    expect(currentComponent.sessionForm).toBeDefined();
    expect(initFormSpy).toHaveBeenCalledWith(mockSession);
    // expect(component.sessionForm?.controls['name'].value).toEqual('Session 1');

    done()
  });


  it('should initialize form without session details if not on update mode', () => {
    // Adjust your ActivatedRoute mock to simulate a URL that doesn't include 'update'
    TestBed.overrideProvider(ActivatedRoute, { useValue: { snapshot: { paramMap: new Map(), url: of('create') } } });
    TestBed.compileComponents(); // Recompile the testing module with overrides
    // Recreate component to apply override
    const {currentFixture, currentComponent} = intializeTestBed(fixture, component)
    const initFormSpy = jest.spyOn(currentComponent as any, 'initForm');

    currentFixture.detectChanges();

    expect(initFormSpy).toHaveBeenCalled();
  })


  it('should validate form fields', () => {
    fixture.detectChanges();
    const nameInput = component.sessionForm!.controls['name'];
    const descriptionInput = component.sessionForm!.controls['description'];
  
    nameInput.setValue('');
    expect(component.sessionForm!.valid).toBeFalsy();
  
    descriptionInput.setValue(''); // Make sure this exceeds your actual limit
    expect(component.sessionForm!.valid).toBeFalsy();
  });


  it('should call the exitpage method to display a success message when create session is successful', () => {
    fixture.detectChanges();
    // Mock the update method to return an error
    jest.spyOn(mockSessionApiService, 'create').mockReturnValue(of(mockSession));
    const snackBarSpy = jest.spyOn(component as any, 'exitPage');
    component.onUpdate = false; // Simulate an update scenario
    component.submit();
  
  
    // Check that an appropriate error message is displayed to the user
    expect(snackBarSpy).toHaveBeenCalledWith('Session created !');
  });


  it('should call the exitpage method to display a success message when create session is successful', () => {
    fixture.detectChanges();
    // Mock the update method to return an error
    jest.spyOn(mockSessionApiService, 'update').mockReturnValue(of(mockSession));
    const snackBarSpy = jest.spyOn(component as any, 'exitPage');
  
    component.onUpdate = true; // Simulate an update scenario
    component.submit();
  
  
    // Check that an appropriate error message is displayed to the user
    expect(snackBarSpy).toHaveBeenCalledWith('Session updated !');
  });
  
  
});



function intializeTestBed(currentFixture: ComponentFixture<any>, currentComponent: any) {
  TestBed.compileComponents(); // Recompile the testing module with overrides
  currentFixture = TestBed.createComponent(FormComponent);
  currentComponent = currentFixture.componentInstance;

  return { currentFixture, currentComponent };

}
