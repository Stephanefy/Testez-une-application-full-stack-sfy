import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';
import { SessionInformation } from '../../../../interfaces/sessionInformation.interface';
import { Session } from '../../interfaces/session.interface';
import { delay, first, Observable, of } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { findEl } from 'src/app/shared/tests/utils';




describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let mockSessionApiService: Partial<SessionApiService>
  let mockSessionService: Partial<SessionService>

  // Mock session data
  const sessionsMock: Session[] = [
    {
      id: 1,
      name: 'Session 1',
      description: 'Description 1',
      date: new Date(),
      teacher_id: 1,
      users: [],
    },
    // Add more sessions as needed
  ];
  



  beforeEach(async () => {

    mockSessionApiService = {
      all(): Observable<Session[]> {
        return of(sessionsMock);
      },
    };

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
    
    }


    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [
        {provide: SessionApiService, useValue: mockSessionApiService }, 
        {provide: SessionService, useValue: mockSessionService }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize sessions$ data on component with data from sessionApiService', (done) => {
    component.sessions$.subscribe(sessions => {
      expect(sessions).toEqual(sessionsMock);
      expect(sessions[0].id).toEqual(1) // check if the first session has an id of 1
      done();
    });
  });

  it('should retrieve user session information correctly', () => {
    expect(component.user).toBeDefined(); // Ensure user information is retrieved
    expect(component.user?.username).toEqual('jodoe');
    expect(component.user?.admin).toBe(true)// Verify specific user information
  });


  it('should display sessions fetched from SessionApiService', (done) => {
    fixture.detectChanges(); // Ensure view reflects the current state
  
    fixture.whenStable().then(() => {
      fixture.detectChanges(); // Update view with async data
      const sessionItems = fixture.nativeElement.querySelectorAll('.item');
      expect(sessionItems.length).toBe(1);
      done();
    });    
  });

  it('should conditionally display the Create and Edit buttons for admin users', () => {

    const createButton = findEl(fixture, "create-button");
    const editButtons = findEl(fixture, "update-button");
    
    expect(createButton).not.toBeNull();
    expect(editButtons).not.toBeNull();
  });
  
  it('should not display Create and Edit buttons if user is not admin', () => {
    // set admin is false
    mockSessionService.sessionInformation = {
      admin: false,
      id: 1,
      token: 'token',
      type: 'type',
      username: 'jodoe',
      firstName: 'John',
      lastName: 'Doe',
    };

    fixture.detectChanges();

    const createButton = findEl(fixture, "create-button");
    const editButtons = findEl(fixture, "update-button");
    
    expect(createButton).toBeNull();
    expect(editButtons).toBeNull();
  })

});
