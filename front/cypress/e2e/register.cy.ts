

describe('Register spec', () => {
    it('should register a user by completing the form and rediect them to login page', () => {
      cy.visit('/register')
  
  
      cy.intercept('POST', '/api/auth/register', {
        fixture:'user.json',
      })
  
      cy.get('input[formControlName=email]').type("john.doe@studio.com")
      cy.get('input[formControlName=firstName]').type("John")
      cy.get('input[formControlName=lastName]').type("Doe")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.url().should('include', '/login')
    })
  });