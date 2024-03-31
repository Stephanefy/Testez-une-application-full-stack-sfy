export {}


import { Session } from "../../src/app/features/sessions/interfaces/session.interface";


declare global {
    
    namespace Cypress {
        interface Chainable<Subject = any> {
          login(): Chainable<any>;
          fetchSessions(): Chainable<any>;
          fetchTeachers(): Chainable<any>;
          createSession(): Chainable<any>;
          fetchDetailsData(): Chainable<any>;
        }
    }

}
  