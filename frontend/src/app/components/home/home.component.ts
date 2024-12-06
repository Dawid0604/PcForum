import { Component, OnInit } from '@angular/core';
import { GroupedThreadCategoryDTO } from '../../model/GroupedThreadCategoryDTO';
import { ThreadCategoryService } from '../../service/thread-category.service';
import { MostPopularThreadDTO } from '../../model/MostPopularThreadDTO';
import { NewestPostDTO } from '../../model/NewestPostDTO';
import { PostService } from '../../service/post.service';
import { ThreadService } from '../../service/thread.service';
import { Title } from '@angular/platform-browser';
import { interval, Subscription, switchMap } from 'rxjs';
import { ActivityUserDTO } from '../../model/ActivitySummaryDTO';
import { UserProfileService } from '../../service/user-profile.service';
import { faCirclePlus } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  threadCategories: GroupedThreadCategoryDTO[] = [];
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
              private postService: PostService,
              private userProfileService: UserProfileService,
              private titleSetter: Title) { }

  ngOnInit(): void {
    this.titleSetter.setTitle("PcForum")    

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
                                                                 });

    this.newestPostsRefreshSubscription = interval(30_000).pipe(switchMap(() => this.postService.findNewestPosts()))
                                                          .subscribe({
                                                             next: _res => this.newestPosts = _res,
                                                             error: _err => console.log(_err)
                                                          });

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
