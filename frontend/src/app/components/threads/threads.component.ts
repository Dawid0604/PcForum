import { Component, OnInit } from '@angular/core';
import { ThreadCategoryService } from '../../service/thread-category.service';
import { ActivatedRoute } from '@angular/router';
import { GroupedThreadCategoryDTO } from '../../model/GroupedThreadCategoryDTO';
import { ThreadDTO } from '../../model/ThreadDTO';
import { ThreadService } from '../../service/thread.service';
import { Pageable } from '../../model/Pageable';
import { MostPopularThreadDTO } from '../../model/MostPopularThreadDTO';
import { NewestPostDTO } from '../../model/NewestPostDTO';
import { PostService } from '../../service/post.service';

@Component({
  selector: 'app-threads',
  templateUrl: './threads.component.html',
  styleUrl: './threads.component.css'
})
export class ThreadsComponent implements OnInit {
  groupedThreadCategory: GroupedThreadCategoryDTO = { } as GroupedThreadCategoryDTO;
  pageableCategoryThreads: Pageable<ThreadDTO> = { } as Pageable<ThreadDTO>;
  mostPopularThreads: MostPopularThreadDTO[] = [];
  newestPosts: NewestPostDTO[] = [];

  constructor(private threadCategoryService: ThreadCategoryService,
              private threadService: ThreadService,
              private router: ActivatedRoute,              
              private postService: PostService) { }

  ngOnInit(): void {
    this.router
        .params
        .subscribe(_params => {
          const categoryId = _params['ref'];

          this.threadCategoryService.findSubCategories(categoryId)
                                    .subscribe({
                                      next: _res => this.groupedThreadCategory = _res,
                                      error: _err => console.log(_err)
                                    })

          this.threadService
              .findAllByCategory(categoryId)
              .subscribe({
                next: _res => this.pageableCategoryThreads = _res,
                error: _err => console.log(_err)
              })

          this.threadService
              .findMostPopularThreads()
              .subscribe({
                next: _res => this.mostPopularThreads = _res,
                error: _err => console.log(_err)
              })
      
          this.postService
              .findNewestPosts()
              .subscribe({
                next: _res => this.newestPosts = _res,
                error: _err => console.log(_err)
              })
        })
  }
}
