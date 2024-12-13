import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ThreadService } from '../../service/thread.service';
import { Pageable } from '../../model/Pageable';
import { ThreadDetailsDTO } from '../../model/ThreadDetailsDTO';
import { PostDTO } from '../../model/PostDTO';
import { PostService } from '../../service/post.service';
import { MostPopularThreadDTO } from '../../model/MostPopularThreadDTO';
import { NewestPostDTO } from '../../model/NewestPostDTO';
import { faCircleCheck, faCirclePlus, faExclamation, faFaceSmileWink, faPenToSquare, faPlay, faQuoteLeft, faShareNodes, faThumbsDown, faThumbsUp, faThumbTack, faThumbTackSlash, faTrash } from '@fortawesome/free-solid-svg-icons';
import { SessionService } from '../../service/session.service';
import { PostReactionService } from '../../service/post-reaction.service';
import { Title } from '@angular/platform-browser';
import { interval, Subscription, switchMap } from 'rxjs';
import { ActivityUserDTO } from '../../model/ActivitySummaryDTO';
import { UserProfileService } from '../../service/user-profile.service';

@Component({
  selector: 'app-single-thread',
  templateUrl: './single-thread.component.html',
  styleUrl: './single-thread.component.css'
})
export class SingleThreadComponent {
  pageablePosts: Pageable<PostDTO> = { } as Pageable<PostDTO>;
  threadDetails: ThreadDetailsDTO = { } as ThreadDetailsDTO;
  mostPopularThreads: MostPopularThreadDTO[] = [];
  newestPosts: NewestPostDTO[] = [];
  form: any = { };
  activityUsers: ActivityUserDTO[] = [];

  upVoteIcon = faCirclePlus;
  activityMode = "";

  weekActivityModeIsSelected = true;
  monthActivityModeIsSelected = false;
  annuallyActivityModeIsSelected = false;
  allTheTimeActivityModeIsSelected = false;

  likeIcon = faThumbsUp;
  dislikeIcon = faThumbsDown;
  unmarkedIcon = faThumbTackSlash;
  markedIcon = faThumbTack;
  shareIcon = faShareNodes;
  createIcon = faPlay;
  editIcon = faPenToSquare;
  removeIcon = faTrash;
  closeThreadIcon = faCircleCheck;

  reportIcon = faExclamation;
  quoteIcon = faQuoteLeft;
  faceSmileWinkIcon = faFaceSmileWink;
  isUserLoggedIn: boolean = false;

  private threadPostsSubscription!: Subscription;
  private mostPopularThreadsRefreshSubscription!: Subscription;
  private newestPostsRefreshSubscription!: Subscription;
  private usersActivityRefreshSubscription!: Subscription;

  constructor(private threadService: ThreadService,
              private postService: PostService,
              private sessionService: SessionService,
              private activatedRouter: ActivatedRoute,
              private postReactionService: PostReactionService,
              private userProfileService: UserProfileService,
              private router: Router,
              private titleSetter: Title) { }

  ngOnInit(): void {
    this.sessionService
        .isUserLoggedIn
        .subscribe(_status => this.isUserLoggedIn = _status);

    this.activatedRouter
        .params
        .subscribe(_params => {
          const threadId = _params['ref'];

          this.threadService.findDetails(threadId)
                            .subscribe({
                              next: _res => {
                                this.threadDetails = _res;
                                this.titleSetter.setTitle("PcForum - " + this.threadDetails.title);
                              },
                              error: _err => console.log(_err)
                            });

          this.loadPosts(threadId);

          this.threadService
              .handleThreadView(threadId)
              .subscribe({
                next: _res => { },
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
    
    this.threadPostsSubscription = interval(30_000).pipe(switchMap(() => this.findPosts(this.threadDetails.encryptedId)))
                                                   .subscribe({
                                                     next: _res => this.pageablePosts = _res,
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
    if(this.threadPostsSubscription) {
      this.threadPostsSubscription.unsubscribe();
    }

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

  create() {
    var payload = {
      threadEncryptedId: this.threadDetails.encryptedId,
      content: this.form.content
    };

    this.postService
        .create(payload)
        .subscribe({
          next: _res => {
            this.loadPosts(payload.threadEncryptedId)
            this.form.content = "";
          },
          error: _err => console.log(_err)
        })
  }

  private loadPosts(threadId: string) {
    this.findPosts(threadId)
        .subscribe({
            next: _res => this.pageablePosts = _res,
            error: _err => console.log(_err)
        });
  }

  private findPosts(threadId: string) {
    return this.postService.findAllByThread(threadId);
  }

  closeThread() {
    this.threadService
        .close(this.threadDetails.encryptedId)
        .subscribe({
          next: _res => this.threadDetails.isClosed = true,
          error: _err => console.log(_err)
        })
  }

  deleteThread() {
    this.threadService
        .delete(this.threadDetails.encryptedId)
        .subscribe({
          next: _res => this.router.navigate([ "" ]),
          error: _err => console.log(_err)
        })
  }

  handleUpVote(post: PostDTO) {
    if(this.isUserLoggedIn && post && !post.loggedUserPost) {
      this.postReactionService
          .handleUpVote(post.encryptedId)
          .subscribe({
            next: _res => this.loadPosts(this.threadDetails.encryptedId),
            error: _err => console.log(_err)
          })
    }
  }

  handleDownVote(post: PostDTO) {
    if(this.isUserLoggedIn && post && !post.loggedUserPost) {
      this.postReactionService
          .handleDownVote(post.encryptedId)
          .subscribe({
            next: _res => this.loadPosts(this.threadDetails.encryptedId),
            error: _err => console.log(_err)
          })
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
