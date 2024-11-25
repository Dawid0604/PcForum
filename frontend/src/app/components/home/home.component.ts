import { Component, OnInit } from '@angular/core';
import { GroupedThreadCategoryDTO } from '../../model/GroupedThreadCategoryDTO';
import { ThreadCategoryService } from '../../service/thread-category.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  threadCategories: GroupedThreadCategoryDTO[] = [];

  constructor(private threadCategoryService: ThreadCategoryService) { }

  ngOnInit(): void {
    this.threadCategoryService
        .findAll()
        .subscribe({
          next: _res => this.threadCategories = _res,
          error: _err => console.log(_err)
        })
  }
}
