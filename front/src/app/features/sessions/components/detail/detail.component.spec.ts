import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';


import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Session } from '../../interfaces/session.interface';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { Overlay } from '@angular/cdk/overlay';
import { findEl, click } from 'src/app/shared/tests/utils';
import { By } from '@angular/platform-browser';

type AdminIdInfo = Pick<SessionInformation, 'admin' | 'id'>;


describe('DetailComponent: unit test with fake logics', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let mockSessionApiService: Partial<SessionApiService>;
  let mockTeacherService: Partial<TeacherService>;
  let mockSessionService: { sessionInformation: AdminIdInfo };
  let navigateSpy : jest.SpyInstance;
  let router: Router;
  let matSnackBar: MatSnackBar;

  beforeEach(async () => {

    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of({
        id: 1,
        name: 'Session 1',
        description: 'Description 1',
        date: new Date(),
        teacher_id: 1,
        users: [],
      })),
      delete: jest.fn().mockReturnValue(of({})),
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({}))
    };

    mockTeacherService = {
      detail: jest.fn().mockReturnValue(of({
        id: 1,
        lastName: 'Doe',
        firstName: 'Teacher',
        createdAt: new Date(),
        updatedAt: new Date(),
      })),
    };

    mockSessionService = {
      sessionInformation: { admin: true, id: 1 }
    };

    const mockActivatedRoute = {
      snapshot: {
        paramMap: new Map([['id', '1']])
      }
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        MatSnackBar
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .compileComponents();
    // service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);


    fixture.detectChanges();
  });


  it('should fetch session on init and set session, isParticipate and teacher data members', async () => {

  const spyMockSessionApiService = jest.spyOn(mockSessionApiService, 'detail');
  const spyMockTeacherService = jest.spyOn(mockTeacherService, 'detail');

  fixture.detectChanges(); // Triggers ngOnInit() which calls fetchSession()

  await fixture.whenStable(); // Waits for all async operations within ngOnInit() to complete

  expect(spyMockSessionApiService).toHaveBeenCalledWith('1');
  expect(component.session).toBeDefined();
  expect(spyMockTeacherService).toHaveBeenCalled();
  expect(component.teacher).toBeDefined();
  });

  it('should delete session, open Material snack bar with correct message and navigate to sessions on success', async () => {
    navigateSpy = jest.spyOn(router, 'navigate');
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');
    
    component.delete();
    fixture.detectChanges();

    console.log(component.isParticipate);

    await fixture.whenStable();

    mockSessionApiService.delete!('1').subscribe(() => {    // expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
      expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
      expect(snackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
    });
  });
});

describe('DetailComponent: integration tests', () => {
  let fixture: ComponentFixture<DetailComponent>;
  let component: DetailComponent;
  let sessionService: SessionService;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let matSnackBar: MatSnackBar;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule
      ],
      declarations: [DetailComponent],
      providers: [
        {provide: SessionService, useValue: {
          sessionInformation: {
            admin: true,
            id: 1,
          }
        }},
        {provide: SessionApiService, useValue: {
          detail: jest.fn().mockReturnValue(of({
            id: 1,
            name: 'Session 1',
            description: 'Description 1',
            date: new Date(),
            teacher_id: 1,
            users: [
              1,3,9,10
            ],
          })),
          delete: () => of({}),
          participate: () => of({}),
          unParticipate: () => of({})
        }},
        TeacherService,
        MatSnackBar,
        FormBuilder,
        Overlay
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .compileComponents();

    sessionService = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    matSnackBar = TestBed.inject(MatSnackBar);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    component.ngOnInit();


  });


  it('should display delete button when current session is admin', () => {
    const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');
    fixture.detectChanges();
  
    const deleteButton = findEl(fixture, 'delete-button');

    expect(fetchSessionSpy).toHaveBeenCalled();  
    expect(deleteButton).not.toBeNull();
  });


  it('should delete a session and show at snackBar with correct message', () => {
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');
    fixture.detectChanges();

    click(fixture, 'delete-button');

  
    expect(snackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
  });
  

})