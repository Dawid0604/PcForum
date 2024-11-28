import { Component } from '@angular/core';
import { MostPopularThreadDTO } from '../../model/MostPopularThreadDTO';
import { NewestPostDTO } from '../../model/NewestPostDTO';
import { ThreadService } from '../../service/thread.service';
import { PostService } from '../../service/post.service';
import { ActivatedRoute } from '@angular/router';
import { CreatorThreadCategoryDTO, CreatorThreadSubCategoryDTO } from '../../model/CreatorThreadCategoryDTO';
import { ThreadCategoryService } from '../../service/thread-category.service';

@Component({
  selector: 'app-thread-creator',
  templateUrl: './thread-creator.component.html',
  styleUrl: './thread-creator.component.css'
})
export class ThreadCreatorComponent {
  mostPopularThreads: MostPopularThreadDTO[] = [];
  newestPosts: NewestPostDTO[] = [];
  categories: CreatorThreadCategoryDTO[] = [];
  subCategories: CreatorThreadSubCategoryDTO[] = [];
  form: any = { };

  constructor(private threadService: ThreadService,
              private threadCategoryService: ThreadCategoryService,
              private postService: PostService,
              private router: ActivatedRoute) { }

  ngOnInit(): void {
    this.router
        .params
        .subscribe(_params => {
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

          this.threadCategoryService
              .findAllCreator()
              .subscribe({
                next: _res => this.categories = _res,
                error: _err => console.log(_err)
              })        
        })
  }

  selectCategory(event: Event) {
    const target = event.target as HTMLSelectElement;
    const categoryId = target.value;
    this.form.category = categoryId;

    this.threadCategoryService
        .findCreatorSubCategories(categoryId)
        .subscribe({
          next: _res => this.subCategories = _res,
          error: _err => console.log(_err)
        })
  }

  selectSubCategory(event: Event) {
    const target = event.target as HTMLSelectElement;
    const categoryId = target.value;
    this.form.subCategory = categoryId;
  }

  create() {
    const payload = {
      encryptedCategoryId: this.form.subCategory,
      title: this.form.title,
      content: this.form.content
    };

    this.threadService
        .create(payload)
        .subscribe({
          next: _res => { },
          error: _err => console.log(_err)
        })
  }
}
