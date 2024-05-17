

# Savasana App

This project is a fork of the Savasana yoga application developed by Numdev, focusing mainly on its testing. Savasana's clients can attend yoga sessions by subscribing to them in this app. On the other hand, teachers working for Savasana can manage their yoga sessions.

## Getting Started

### Installation

This repository contains both the frontend and backend of the application. Follow the instructions below to correctly install the project.

1. Clone this repository:
   ```sh
   git clone https://github.com/Stephanefy/Testez-une-application-full-stack-sfy.git
   ```

2. Install dependencies and build both projects (frontend and backend):

   - Frontend:
     ```sh
     cd front
     npm install
     ```

   - Backend:
     ```sh
     cd back
     mvn clean install
     ```

## Database Configuration

### Application Database

To run the application, create a MySQL database named `test` with a connecting profile using the same username and password as predefined in the `application.properties` file.

### Integration testing on Database on the backend

For integration testing purposes, an embedded H2 database is automatically created and dropped on each integration test.

## Running Tests

### Backend

Run the following command to execute all tests on the backend:
```sh
cd back
mvn clean test
```

This will automatically generate a test coverage report. You can access the report in a browser by opening the HTML file at the following path:
```sh
cd back/target/site/jacoco/index.html
```

### Frontend

Run the following command to execute all tests on the frontend and directly generate a coverage report in the terminal:

```sh
cd front
npm test --coverage
```

Once the tests are run and coverage is generated, you can check the coverage report by opening the HTML file in a browser at the following path:

```sh
cd front/jest/lcov-report/index.html
```
#### Cypress runner
To launch Cypress and run the end-to-end tests, use the following command:

```sh
cd front
npm run e2e
```

In the Cypress runner, go to the ‘Specs’ tab, hover over the test suite title on the right, and click on ‘run 5 tests’.

#### In terminal
If you wish to just run the e2e tests without running Cypress driver, use these following commands:

First start the application

```sh
npm start
```

Open another terminal from the front directory and run:

```sh  
npm run cypress:run
```

#### E2E coverage report

The coverage for the e2e tests is available at this path:

```sh 
cd front/coverage/lcov-report/index.html
```

