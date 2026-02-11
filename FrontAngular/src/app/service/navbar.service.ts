import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class NavbarService {
  toggleEvent : BehaviorSubject<boolean>;

  constructor() {
    this.toggleEvent = new BehaviorSubject<boolean>(true);
  }

  showNavbar() {
    this.toggleEvent.next(true);
  }

  hideNavbar() {
    this.toggleEvent.next(false);
  }

}
