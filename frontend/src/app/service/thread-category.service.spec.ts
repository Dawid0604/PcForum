import { TestBed } from '@angular/core/testing';

import { ThreadCategoryService } from './thread-category.service';

describe('ThreadCategoryService', () => {
  let service: ThreadCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ThreadCategoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
