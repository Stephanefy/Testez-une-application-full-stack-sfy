import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';

describe('TeacherService', () => {
  let service: TeacherService;
  let controller: HttpTestingController

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      providers: [TeacherService],
    });
    service = TestBed.inject(TeacherService);
    controller = TestBed.inject(HttpTestingController);

  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all teachers', () => {
    const mockTeachers = [{ id: '1', name: 'John Doe' }];
  
    service.all().subscribe(teachers => {
      expect(teachers.length).toBe(1);
      expect(teachers).toEqual(mockTeachers);
    });
  
    const req = controller.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers);
  });

  it('should retrieve teacher details by id', () => {
    const mockTeacher = { id: '1', name: 'John Doe' };
  
    service.detail('1').subscribe(teacher => {
      expect(teacher).toEqual(mockTeacher);
    });
  
    const req = controller.expectOne(`api/teacher/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher);
  });
  
  
});
