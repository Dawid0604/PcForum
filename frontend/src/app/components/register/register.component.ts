import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  constructor(private titleSetter: Title) { }
    
  ngOnInit(): void {
    this.titleSetter.setTitle("PcForum - registration")
  }
}
