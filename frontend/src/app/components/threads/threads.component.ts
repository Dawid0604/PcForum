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
import { Title } from '@angular/platform-browser';
import { interval, Subscription, switchMap } from 'rxjs';
import { ActivityUserDTO } from '../../model/ActivitySummaryDTO';
import { faCirclePlus } from '@fortawesome/free-solid-svg-icons';
import { UserProfileService } from '../../service/user-profile.service';

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

  constructor(private threadCategoryService: ThreadCategoryService,
              private threadService: ThreadService,
              private userProfileService: UserProfileService,
              private router: ActivatedRoute,              
              private postService: PostService,
              private titleSetter: Title) { }

  ngOnInit(): void {
    this.router
        .params
        .subscribe(_params => {
          const categoryId = _params['ref'];

          this.threadCategoryService.findSubCategories(categoryId)
                                    .subscribe({
                                      next: _res => {
                                        this.groupedThreadCategory = _res;
                                        this.titleSetter.setTitle("PcForum - " + this.groupedThreadCategory.name);
                                      },
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
