import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserProfileVisitorsComponent } from './user-profile-visitors.component';

describe('UserProfileVisitorsComponent', () => {
  let component: UserProfileVisitorsComponent;
  let fixture: ComponentFixture<UserProfileVisitorsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserProfileVisitorsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserProfileVisitorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
