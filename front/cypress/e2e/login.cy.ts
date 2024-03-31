import '../support/commands';

describe('Login spec', () => {


  
  it('Login successfull', () => {
    cy.login()
    
    cy.url().should('include', '/sessions')
  })

  it('should display en error occured if email format is not valid', () => {

    cy.visit('/login')


    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studiocom")
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('[type="submit"]').click();


    cy.get('[data-cy="login-error"]').should('be.visible')
  })
});