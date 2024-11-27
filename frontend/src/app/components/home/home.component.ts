import { Component, OnInit } from '@angular/core';
import { GroupedThreadCategoryDTO } from '../../model/GroupedThreadCategoryDTO';
import { ThreadCategoryService } from '../../service/thread-category.service';
import { MostPopularThreadDTO } from '../../model/MostPopularThreadDTO';
import { NewestPostDTO } from '../../model/NewestPostDTO';
import { PostService } from '../../service/post.service';
import { ThreadService } from '../../service/thread.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  threadCategories: GroupedThreadCategoryDTO[] = [];
  mostPopularThreads: MostPopularThreadDTO[] = [];
  newestPosts: NewestPostDTO[] = [];

  constructor(private threadCategoryService: ThreadCategoryService,
              private threadService: ThreadService,
              private postService: PostService) { }

  ngOnInit(): void {
    this.threadCategoryService
        .findAll()
        .subscribe({
          next: _res => this.threadCategories = _res,
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
  }
}
