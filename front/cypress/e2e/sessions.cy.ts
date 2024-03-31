import '../support/commands';

describe('Session list spec', () => {
  it('should show correct number of sessions when user log in', () => {

    cy.fetchSessions();
    cy.login().as('user');

    
    
    cy.get('[data-cy="session-card"]').should('have.length', 3);


  });

  it('it should login an admin user then navigate to create session page, fill the form and redirect to session list with the latest created session', () => {
    cy.fetchTeachers();
    cy.login();


    cy.get('[data-cy="create-button"]').click();
    cy.url().should('include', '/sessions/create');

    cy.get('input[formControlName=name]').type('Session 4');
    cy.get('input[formControlName=date]').click().type('2024-04-30');
    cy.get('[data-cy="teacher-select"]').click();
    cy.get('mat-option').contains("Alice Doe").click();
    cy.get('[data-cy="description-textarea"]').type('Description 4');


    cy.get('[data-cy="submit-button-form"]').click();

    
    cy.createSession();

    cy.intercept('GET', '/api/session', {
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
    }).as('session');

    cy.get('button[data-cy="submit-button-form"]').click();



    cy.get('[data-cy="session-card"]').should('have.length', 1);


  });

  it('should show an error message when form is not completely filled', () => {

    cy.login();

    cy.fetchTeachers();

    cy.get('[data-cy="create-button"]').click();
    cy.url().should('include', '/sessions/create');

    cy.get('input[formControlName=name]').type('Session 4');
    cy.get('input[formControlName=date]').click().type('2024-04-30');
    cy.get('[data-cy="teacher-select"]').click();
    cy.get('mat-option').contains("Alice Doe").click();


    cy.get('[data-cy="submit-button-form"]').should('be.disabled');
  })


  it('should allow user to delete a session then when returned to session page the session should be gone', () => {

    cy.intercept('GET', '/api/session', {
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
    }).as('session');

    cy.login();

    cy.fetchDetailsData();



    cy.get('[data-cy="session-card"]').should('have.length', 1);
    cy.get('[data-cy="detail-button"]').click();

    cy.get('[data-cy="details-session-name"').should('contain', 'Session 4');
    cy.get('[data-cy="details-session-description"').should('contain', 'Description 4');

    cy.intercept(
      {
        method: 'DELETE',
        url: '/api/session/4',
      },
      []
    ).as('session');


    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    cy.get('[data-cy="details-delete-button"]').click();

  })


  it('should allow user to edit a session then when returning to the sessions page the session should be edited', () => {
    // fetch mock data that will populate the form
    cy.intercept('GET', '/api/session', {
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
    }).as('session');
    cy.fetchTeachers();

    cy.login();
    cy.fetchDetailsData();

    cy.get('[data-cy="edit-button"]').click();
    cy.url().should('include', '/sessions/update/4');

    cy.get('input[formControlName=name]').clear().type('Session 4 Warrior position');
    cy.get('[data-cy="teacher-select"]').click();
    cy.get('mat-option').contains("Alice Doe").click();


    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 4,
          name: 'Session 4 Warrior position',
          description: 'Description 4',
          date: '2024-04-30',
          teacher_id: 1,
          users: [],
        },
      ],
    }).as('session');


    cy.get('button[data-cy="submit-button-form"]').click();

    cy.get('[data-cy="form-back-button"]').click();


  })
});
