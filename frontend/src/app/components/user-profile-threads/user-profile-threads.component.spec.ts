import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserProfileThreadsComponent } from './user-profile-threads.component';

describe('UserProfileThreadsComponent', () => {
  let component: UserProfileThreadsComponent;
  let fixture: ComponentFixture<UserProfileThreadsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserProfileThreadsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserProfileThreadsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
