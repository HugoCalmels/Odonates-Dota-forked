import { Component, ElementRef, HostListener, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';
import { Subject, filter, takeUntil } from 'rxjs';
import { AlertFacade } from 'src/app/store/alert/alert.facade';

@Component({
  selector: 'app-custom-alert',
  templateUrl: './custom-alert.component.html',
  styleUrls: ['./custom-alert.component.scss']
})
export class CustomAlertComponent implements OnInit, OnDestroy {
  @Input() alertType: string | null = null;
  @Input() message: string | null = null;
  @Output() close = new EventEmitter<void>();
  isVisible: boolean = false;
  private unsubscribe$: Subject<void> = new Subject<void>();

  constructor(
    private el: ElementRef,
    private alertFacade: AlertFacade,
    private router: Router
  ) { }

  ngOnInit() {
    setTimeout(() => {
      this.isVisible = true;
    }, 100);

    this.router.events.pipe(
      filter(event => event instanceof NavigationStart),
      takeUntil(this.unsubscribe$)
    ).subscribe(() => {
      this.alertFacade.clearAlert();
    });
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    if (!this.el.nativeElement.contains(event.target) && this.isVisible) {
      this.alertFacade.clearAlert();
    }
  }

}