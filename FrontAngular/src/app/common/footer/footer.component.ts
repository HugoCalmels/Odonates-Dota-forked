import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CguModalComponent } from './cgu-modal/cgu-modal.component';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {

  data: Date = new Date();

  constructor(public dialog: MatDialog) { }

  openCguModal(): void {
    this.dialog.open(CguModalComponent, {
      width: '60vw',
      height: '60vh',
      autoFocus: false,
      data: {}
    });
  }
}