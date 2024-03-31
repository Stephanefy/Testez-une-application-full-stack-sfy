import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';
import { mockSessions, mockSession1 } from 'src/app/shared/tests/mocks';

describe('SessionsService', () => {
  let service: SessionApiService;
  let controller: HttpTestingController;



  beforeEach(() => {

    

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService],
    });
    service = TestBed.inject(SessionApiService);
    controller = TestBed.inject(HttpTestingController);

  });

  it('should returns all sessions', () => {


    service.all().subscribe((sessions) => {
      expect(sessions.length).toBe(2);
      expect(sessions).toEqual(mockSessions);
    });

    const req = controller.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });
  it('should returns one session by id', () => {
    service.detail('1').subscribe(session => {
      expect(session).toEqual(mockSessions[0]);
    });
  
    const req = controller.expectOne(`api/session/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions[0]);
  });
  it('should delete a session by id', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeNull();
    });
  
    const req = controller.expectOne(`api/session/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null); //
  });
  it('should create a new session', () => {
    const newMockSession: Session = {
      id: 3,
      name: 'Session 3',
      description: 'Description session 3',
      date: new Date(),
      teacher_id: 1,
      users: [],
    };
    service.create(newMockSession).subscribe((session) => {
      expect(mockSessions).toHaveLength(3);
    });
    const req = controller.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush(newMockSession);
  });
  it('should update a session by providing and id and the updated session', () => {
    const mockUpdatedSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'updated description session 1',
      date: new Date(),
      teacher_id: 1,
      users: [],
    };
    service.update("1", mockSession1).subscribe((session) => {
      expect(session).toEqual(mockUpdatedSession);
    });
    const req = controller.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    req.flush(mockUpdatedSession);

  });
  it('should set unparticipation of a user from a session', () => {
    
    service.unParticipate("1", "1").subscribe((session) => {
      expect(mockSession1.users).toHaveLength(0);
    });
    const req = controller.expectOne('api/session/1/participate/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(mockSession1);

  });
  it('should set participation of user to a session', () => {
    // simulate participation of user 1 to the session 1
    mockSession1.users.push(1)

    service.participate("1", "1").subscribe((session) => {
      expect(mockSession1.users).toHaveLength(1);
    });
    const req = controller.expectOne('api/session/1/participate/1');
    expect(req.request.method).toBe('POST');
    req.flush(mockSession1);

  });
});
