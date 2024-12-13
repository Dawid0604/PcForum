import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BrowserResultDTO } from '../../model/BrowserResultDTO';
import { BrowserService } from '../../service/browser.service';
import { faCircleUser, faCommentMedical, faComments, faUserLarge } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-browser',
  templateUrl: './browser.component.html',
  styleUrl: './browser.component.css'
})
export class BrowserComponent implements OnInit {
  foundResults: BrowserResultDTO = { } as BrowserResultDTO;
  searchedText = "";

  postsIcon = faComments;
  threadsIcon = faCommentMedical;
  userProfileIcon = faCircleUser;

  constructor(private activatedRoute: ActivatedRoute,
              private browserService: BrowserService) { }

  ngOnInit(): void {
    this.activatedRoute
        .paramMap
        .subscribe(_params => {
          const param = _params.get("text");

          if(param) {
            this.searchedText = param;
            this.browserService
                .find(this.searchedText)
                .subscribe({
                  next: _res => this.foundResults = _res,
                  error: _err => console.log(_err)
                })
          }
        })
  }

}
