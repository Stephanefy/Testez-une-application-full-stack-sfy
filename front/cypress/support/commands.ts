import { Session } from '../../src/app/features/sessions/interfaces/session.interface';
// ***********************************************
// This example namespace declaration will help
// with Intellisense and code completion in your
// IDE or Text Editor.
// ***********************************************
// declare namespace Cypress {
//   interface Chainable<Subject = any> {
//     customCommand(param: any): typeof customCommand;
//   }
// }
//
// function customCommand(param: any): void {
//   console.warn(param);
// }
//
// NOTE: You can use it like so:
// Cypress.Commands.add('customCommand', customCommand);
//
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })




Cypress.Commands.add('login' as any, () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
  });



Cypress.Commands.add('fetchSessions'as any, () => {
  cy.intercept(
    {
      method: 'GET',
      url: '/api/session',
    },
    [
      {
        id: 1,
        name: 'Session 1',
        description: 'Description 1',
        date: '21/03/2025',
        teacher_id: 1,
        users: [],
      },
      {
        id: 2,
        name: 'Session 2',
        description: 'Description 2',
        date: '21/03/2025',
        teacher_id: 1,
        users: [],
      },
      {
        id: 3,
        name: 'Session 3',
        description: 'Description 3',
        date: '21/03/2025',
        teacher_id: 1,
        users: [],
      },
    ]
  ).as('session')
})


Cypress.Commands.add('fetchTeachers' as any, () => {

  cy.intercept('GET', '/api/teacher', {
    body: [
      {
        id: 1,
        lastName: 'Doe',
        firstName: 'Johnas',
        createdAt: '2022-03-21T10:48:17.000Z',
        updatedAt: '2022-03-21T10:48:17.000Z',
      },
      {
        id: 2,
        lastName: 'Doe',
        firstName: 'Alice',
        createdAt: '2022-03-21T10:48:17.000Z',
        updatedAt: '2022-03-21T10:48:17.000Z',
      },
      {
        id: 2,
        lastName: 'Doe',
        firstName: 'Bob',
        createdAt: '2022-03-21T10:48:17.000Z',
        updatedAt: '2022-03-21T10:48:17.000Z',
      },
    ],
  }).as("teacher");
})

Cypress.Commands.add('createSession' as any, () => {
  cy.intercept('POST', '/api/session', {
    body: [
      {
        id: 4,
        name: 'Session 4',
        description: 'Description 4',
        date: '2024-04-30',
        teacher_id: 1,
        users: [],
      },
    ],
  });
})


Cypress.Commands.add('fetchDetailsData' as any, () => {

  cy.intercept('GET', '/api/session/4', {
    body: 
      {
        id: 4,
        name: 'Session 4',
        description: 'Description 4',
        date: '2024-04-30',
        teacher_id: 1,
        users: [],
        createdAt: '2022-03-21',
        updatedAt: '2022-03-21',
      }
    }).as('session-details');

  cy.intercept('GET', '/api/teacher/1', {
    body: [
      {
        id: 1,
        lastName: "Doe",
        firstName: "Anna",
        createdAt: "2022-03-21",
        updatedAt: "2022-03-21",
      },
    ],
  }).as('teacher');
})
  