import {Component, HostListener, OnInit} from '@angular/core';
import {NavbarService} from "./service/navbar.service";
import {AuthService} from './service/auth/auth.service';

let lastScrollTop = 0;
let delta = 5;
let navbarHeight = 0;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{

  constructor(private navbarService: NavbarService,
              private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.authService.initializeAuthentication();
  }

  @HostListener('window:scroll', ['$event'])
  hasScrolled() {
    const st = window.scrollY;
    // Make sure they scroll more than delta
    if (Math.abs(lastScrollTop - st) <= delta)
      return;

    if (st > lastScrollTop && st > navbarHeight) {
      this.navbarService.hideNavbar();
    } else {
      if (st + window.innerHeight < document.body.scrollHeight) {
        this.navbarService.showNavbar();
      }
    }

    lastScrollTop = st;
  };

}
