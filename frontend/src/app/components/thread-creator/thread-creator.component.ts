import { Component } from '@angular/core';
import { MostPopularThreadDTO } from '../../model/MostPopularThreadDTO';
import { NewestPostDTO } from '../../model/NewestPostDTO';
import { ThreadService } from '../../service/thread.service';
import { PostService } from '../../service/post.service';
import { ActivatedRoute } from '@angular/router';
import { CreatorThreadCategoryDTO, CreatorThreadSubCategoryDTO } from '../../model/CreatorThreadCategoryDTO';
import { ThreadCategoryService } from '../../service/thread-category.service';
import { interval, Subscription, switchMap } from 'rxjs';
import { UserProfileService } from '../../service/user-profile.service';
import { ActivityUserDTO } from '../../model/ActivitySummaryDTO';
import { faCirclePlus } from '@fortawesome/free-solid-svg-icons';

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
  activityUsers: ActivityUserDTO[] = [];

  upVoteIcon = faCirclePlus;
  activityMode = "";

  weekActivityModeIsSelected = true;
  monthActivityModeIsSelected = false;
  annuallyActivityModeIsSelected = false;
  allTheTimeActivityModeIsSelected = false;

  private mostPopularThreadsRefreshSubscription!: Subscription;
  private newestPostsRefreshSubscription!: Subscription;
  private usersActivityRefreshSubscription!: Subscription;

  constructor(private threadService: ThreadService,
              private threadCategoryService: ThreadCategoryService,
              private postService: PostService,
              private userProfileService: UserProfileService,
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

        
        this.userProfileService
            .getActivitySummary(this.activityMode)
            .subscribe({
              next: _res => this.activityUsers = _res.users,
              error: _err => console.log(_err)
            })

        this.mostPopularThreadsRefreshSubscription = interval(30_000).pipe(switchMap(() => this.threadService.findMostPopularThreads()))
                                                                     .subscribe({
                                                                        next: _res => this.mostPopularThreads = _res,
                                                                        error: _err => console.log(_err)
                                                                     })

        this.newestPostsRefreshSubscription = interval(30_000).pipe(switchMap(() => this.postService.findNewestPosts()))
                                                              .subscribe({
                                                                 next: _res => this.newestPosts = _res,
                                                                 error: _err => console.log(_err)
                                                              })
        
        this.usersActivityRefreshSubscription = interval(30_000).pipe(switchMap(() => this.userProfileService.getActivitySummary(this.activityMode)))
                                                                .subscribe({
                                                                    next: _res => this.activityUsers = _res.users,
                                                                    error: _err => console.log(_err)
                                                                });                                                              
  }

  ngOnDestroy(): void {    
    if(this.mostPopularThreadsRefreshSubscription) {
      this.mostPopularThreadsRefreshSubscription.unsubscribe();
    }
    
    if(this.newestPostsRefreshSubscription) {
      this.newestPostsRefreshSubscription.unsubscribe();
    }

    if(this.usersActivityRefreshSubscription) {
      this.usersActivityRefreshSubscription.unsubscribe();
    }
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
      encryptedCategoryId: (this.form.subCategory ? this.form.subCategory : this.form.category),
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

  selectWeekActivityMode() {
    this.activityMode = "WEEK";
    this.weekActivityModeIsSelected = true;
    this.monthActivityModeIsSelected = false;
    this.annuallyActivityModeIsSelected = false;
    this.allTheTimeActivityModeIsSelected = false;

    this.userProfileService
        .getActivitySummary(this.activityMode)
        .subscribe({
          next: _res => this.activityUsers = _res.users,
          error: _err => console.log(_err)
        })
  }

  selectMonthActivityMode() {
    this.activityMode = "MONTH";
    this.weekActivityModeIsSelected = false;
    this.monthActivityModeIsSelected = true;
    this.annuallyActivityModeIsSelected = false;
    this.allTheTimeActivityModeIsSelected = false;

    this.userProfileService
        .getActivitySummary(this.activityMode)
        .subscribe({
          next: _res => this.activityUsers = _res.users,
          error: _err => console.log(_err)
        })
  }

  selectAnnuallyActivityMode() {
    this.activityMode = "ANNUALLY";
    this.weekActivityModeIsSelected = false;
    this.monthActivityModeIsSelected = false;
    this.annuallyActivityModeIsSelected = true;
    this.allTheTimeActivityModeIsSelected = false;

    this.userProfileService
        .getActivitySummary(this.activityMode)
        .subscribe({
          next: _res => this.activityUsers = _res.users,
          error: _err => console.log(_err)
        })
  }

  selectAllTheTimeActivityMode() {
    this.activityMode = "ALL_THE_TIME";
    this.weekActivityModeIsSelected = false;
    this.monthActivityModeIsSelected = false;
    this.annuallyActivityModeIsSelected = false;
    this.allTheTimeActivityModeIsSelected = true;

    this.userProfileService
        .getActivitySummary(this.activityMode)
        .subscribe({
          next: _res => this.activityUsers = _res.users,
          error: _err => console.log(_err)
        })
  }
}
