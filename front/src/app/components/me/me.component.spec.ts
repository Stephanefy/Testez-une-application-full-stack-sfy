import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { User } from 'src/app/interfaces/user.interface';
import { UserService } from 'src/app/services/user.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { Location } from "@angular/common";
import { routes } from 'src/app/app-routing.module';
import { RouterTestingModule } from '@angular/router/testing';


describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let location: Location;
  let router: Router;
  // let snackBar : MatSnackBarHarness;

  const userMock: Omit<User, 'password'> = {id: 1 , admin: true, firstName: 'john', lastName: 'Doe', email: 'johnDoe@test.com', createdAt: new Date(), updatedAt: new Date()}
  const mockUserService = {
    getById: jest.fn().mockReturnValue(of(userMock)),
    delete: jest.fn().mockReturnValue(of(null))
  }

  const mockMatSnackBar = {
    open: jest.fn()
  };
  
  const mockRouter = {
    navigate: jest.fn()
  };
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule.withRoutes(routes)
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService},
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    router = TestBed.inject(Router);
    location = TestBed.inject(Location);
    component = fixture.componentInstance;
    fixture.detectChanges();



  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should fetch user on ngOnInit by getting the id from session information', () => {
      
      expect(mockUserService.getById).toHaveBeenCalledWith(mockSessionService.sessionInformation!.id.toString());
      expect(component.user).toEqual(userMock);
  })


  it('should delete user and show confirmation snack bar and redirect to home page', async () => {


    // Trigger the delete method
    await component.delete();
  
    // Verify userService.delete was called
    expect(mockUserService.delete).toHaveBeenCalledWith(mockSessionService.sessionInformation!.id.toString());
    // Check MatSnackBar was called with the correct message
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    // // Check navigation to the root
    router.navigate(["/"])

    expect(location.path()).toBe("");
  });
  
});
