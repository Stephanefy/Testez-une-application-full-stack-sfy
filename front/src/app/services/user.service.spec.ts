import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { mockUser } from 'src/app/shared/tests/mocks';

describe('UserService', () => {
  let service: UserService;
  let controller: HttpTestingController

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
    controller = TestBed.inject(HttpTestingController);
  });

  it('should retrieve user by ID', () => {
  
    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUser);
    });
  
    const req = controller.expectOne(`api/user/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser); // Simulate a successful response with mockUser
  });

  it('should delete user by ID', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeNull(); // Assuming the delete operation returns an empty response
    });
  
    const req = controller.expectOne(`api/user/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null); // Simulate a successful empty response
  });
  
  
});
