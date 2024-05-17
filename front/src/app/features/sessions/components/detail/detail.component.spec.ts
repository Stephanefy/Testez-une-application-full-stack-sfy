import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { Location } from "@angular/common";


import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { findEl, click, expectText } from 'src/app/shared/tests/utils';

type AdminIdInfo = Pick<SessionInformation, 'admin' | 'id'>;

describe('DetailComponent: unit test with fake logics', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let mockSessionApiService: Partial<SessionApiService>;
  let mockTeacherService: Partial<TeacherService>;
  let mockSessionService: { sessionInformation: AdminIdInfo };
  let navigateSpy: jest.SpyInstance;
  let router: Router;
  let matSnackBar: MatSnackBar;
  let location: Location;


  const mockSession = {
    id: 1,
    name: 'Session 1',
    description: 'Description 1',
    date: new Date(),
    teacher_id: 1,
    users: [],
  };

  beforeEach(async () => {
    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of(mockSession)),
      delete: jest.fn().mockReturnValue(of({})),
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({})),
    };

    mockTeacherService = {
      detail: jest.fn().mockReturnValue(
        of({
          id: 1,
          lastName: 'Doe',
          firstName: 'Teacher',
          createdAt: new Date(),
          updatedAt: new Date(),
        })
      ),
    };

    mockSessionService = {
      sessionInformation: { admin: true, id: 1 },
    };

    const mockActivatedRoute = {
      snapshot: {
        paramMap: new Map([['id', '1']]),
      },
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        MatSnackBar,
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    location = TestBed.inject(Location);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);

    fixture.detectChanges();
  });

  it('should display delete button if current session user is admin', () => {
    component.isAdmin = true;
    const deleteBtn = findEl(fixture, 'delete-button');

    expect(deleteBtn).toBeDefined();
  });

  it('should not display delete button if current session user is not admin', () => {
    component.isAdmin = false;
    fixture.detectChanges();

    const deleteBtn = findEl(fixture, 'delete-button');

    expect(deleteBtn).toBeNull();
  });

  it('should display current session information', () => {
    expectText(fixture, 'session-description', mockSession.description);
    expectText(fixture, 'session-name', mockSession.name);
    expectText(
      fixture,
      'session-attendees',
      `${mockSession.users.length.toString()} attendees`
    );
  });

  it('should call participate in a session with current session id and current user id', () => {
    component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');
  });

  it('should call unParticipate in a session with current session id and current user id', () => {
    component.unParticipate();
    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
  });

  it('should fetch session on init and set session, isParticipate and teacher data members', async () => {
    const spyMockSessionApiService = jest.spyOn(
      mockSessionApiService,
      'detail'
    );
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


    await fixture.whenStable();

    mockSessionApiService.delete!('1').subscribe(() => {
      expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
      expect(snackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', {
        duration: 3000,
      });
      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
      expect(location.path()).toBe("/sessions");

    });
  });
});
