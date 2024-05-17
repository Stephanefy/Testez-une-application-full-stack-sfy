import { Observable, of } from "rxjs";
import { Session } from "src/app/features/sessions/interfaces/session.interface";
import { SessionInformation } from "src/app/interfaces/sessionInformation.interface";
import { User } from "src/app/interfaces/user.interface";
import { SessionService } from "src/app/services/session.service";


/** use in session-api service **/
export const mockSessions: Session[] = [
    {
      id: 1,
      name: 'Session 1',
      description: 'Description session 1',
      date: new Date(),
      teacher_id: 1,
      users: [],
    },
    {
      id: 2,
      name: 'Session 2',
      description: 'Description session 2',
      date: new Date(),
      teacher_id: 1,
      users: [],
    },
  ];

export const mockSession1: Session = {
    id: 1,
    name: 'Session 1',
    description: 'updated description session 1',
    date: new Date(),
    teacher_id: 1,
    users: [],
  }
/** */

/** use in session service */

export const mockUserSessionInfo: SessionInformation = {
    admin: true,
    id: 1,
    username: 'testUser',
    firstName: 'John',
    lastName: 'Doe',
    token: 'token',
    type: 'type',
  };
/** */

/** use in user service */

export const mockUser: User = {
    id: 1,
    email: 'testUser',
    lastName: 'Doe',
    firstName: 'John',
    admin: true,
    password: 'password',
    createdAt: new Date(),
    updatedAt: new Date(),
  };
/** */


export const mockSessionService: Partial<SessionService> = {
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
