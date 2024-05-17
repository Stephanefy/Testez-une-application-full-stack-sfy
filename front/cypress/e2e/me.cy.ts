describe('Me spec', () => {
    it('should log user in and when they click on account redirect them to their account', () => {

        cy.visit('/login')


        cy.intercept('POST', '/api/auth/login', {
          body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: true
          },
        })
    
        cy.intercept(
          {
            method: 'GET',
            url: '/api/session',
          },
          []).as('session')
        
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        
        cy.url().should('include', '/sessions')

        cy.intercept('GET', '/api/user/1', {
          body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            email: 'userName@test.com',
            admin: false,
            createdAt: '2022-01-01',
            updatedAt: '2022-01-01',
          },
        })

        cy.get('[routerLink="me"]').click();

        cy.url().should('include', '/me')

        cy.get('[data-testid=full-username').should('contain', 'Name: firstName LASTNAME')
        cy.get('[data-testid="email"]').should('contain', 'userName@test.com');
        
       
    })

    it("should login then navigate to the me page then delete when the button is clicked", () => {
      cy.visit('/login')


      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: false
        },
      })



      cy.get('input[formControlName=email]').type("userName@test.com")
      cy.get('input[formControlName=password]').type(`${"password123!"}{enter}{enter}`)
      


      cy.intercept('GET', '/api/user/1', {
        body: {
          id: 1,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: false
        },
      })

      cy.get('[routerLink="me"]').click();

      cy.intercept('DELETE', '/api/user/1', {
        statusCode: 200,
        body: {},
      })

      cy.get('[data-cy="delete-button"]').should('be.visible');


      cy.get('[data-cy="delete-button"]').click();
      cy.get('.mat-snack-bar-container').should('contain', 'Your account has been deleted !');
      

      


      cy.url().should('include', '/')

    })
})