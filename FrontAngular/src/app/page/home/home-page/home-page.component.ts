import {Component, OnInit} from '@angular/core';
import ScrollUtil from "../../../utils/ScrollUtil";
import {DomSanitizer, SafeStyle} from "@angular/platform-browser";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss']
})
export class HomePageComponent implements OnInit {

  backgroundImageUrl: SafeStyle | undefined = undefined;
  constructor(private sanitizer: DomSanitizer) {
  }
  ngOnInit() {
    const imageUrl = '/assets/img/dota2.jpg';
    this.backgroundImageUrl = this.sanitizer.bypassSecurityTrustStyle(`url(${imageUrl})`);
  }


  // Sanitize the URL to make it safe to use in the style

  smoothScroll(target: any) {
    let elem = document.getElementById(target);
    if (elem) {
      ScrollUtil.scrollTo(document.scrollingElement || document.documentElement, elem.offsetTop, 1250);
    }
  }
}
