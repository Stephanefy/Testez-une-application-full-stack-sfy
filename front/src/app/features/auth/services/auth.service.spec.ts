import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { mockUserSessionInfo } from 'src/app/shared/tests/mocks';

describe('AuthService', () => {
  let service: AuthService;
  let httpController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService],
    });

    service = TestBed.inject(AuthService);
    httpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Ensure there are no outstanding HTTP calls after each test
    httpController.verify();
  });

  it('should send register request and receive a response', () => {
    const mockRegisterRequest: RegisterRequest = {
      email: 'testEmail',
      firstName: 'testFirstName',
      lastName: 'testLastName',
      password: 'testPassword',
    };
  
    service.register(mockRegisterRequest).subscribe(response => {
      expect(response).toBeUndefined();
    });
  
    const req = httpController.expectOne(`${service['pathService']}/register`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRegisterRequest);
    req.flush(null); // Simulate the backend returning a void response
  });

  it('should send login request and receive session information', () => {
    const mockLoginRequest: LoginRequest = {
      email: 'test@example.com',
      password: 'password'
    };
  
   
  
    service.login(mockLoginRequest).subscribe(sessionInfo => {
      expect(sessionInfo).toEqual(mockUserSessionInfo);
    });
  
    const req = httpController.expectOne(`${service['pathService']}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockLoginRequest);
    req.flush(mockUserSessionInfo); // Simulate the backend returning session information
  });
  
});


  