import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ThreadService } from '../../service/thread.service';
import { Pageable } from '../../model/Pageable';
import { ThreadDetailsDTO } from '../../model/ThreadDetailsDTO';
import { PostDTO } from '../../model/PostDTO';
import { PostService } from '../../service/post.service';

@Component({
  selector: 'app-single-thread',
  templateUrl: './single-thread.component.html',
  styleUrl: './single-thread.component.css'
})
export class SingleThreadComponent {
  pageablePosts: Pageable<PostDTO> = { } as Pageable<PostDTO>;
  threadDetails: ThreadDetailsDTO = { } as ThreadDetailsDTO;

  constructor(private threadService: ThreadService,
              private postService: PostService,
              private router: ActivatedRoute) { }

  ngOnInit(): void {
    this.router
        .params
        .subscribe(_params => {
          const threadId = _params['ref'];

          this.threadService.findDetails(threadId)
                            .subscribe({
                              next: _res => this.threadDetails = _res,
                              error: _err => console.log(_err)
                            });

          this.postService.findAllByThread(threadId)
                                    .subscribe({
                                      next: _res => this.pageablePosts = _res,
                                      error: _err => console.log(_err)
                                    });
        })
  }
}