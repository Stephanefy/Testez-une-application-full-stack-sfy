describe('Not Found Page', () => {
    it('should display the 404 page when navigating to a non-existent route', () => {
      cy.visit('/non-existent-route', { failOnStatusCode: false });
  
      cy.get('h1').should('contain', 'Page not found !');
    });
  });
  