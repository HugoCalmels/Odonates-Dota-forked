import { Component, Output, EventEmitter } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-cgu-modal',
  templateUrl: './cgu-modal.component.html',
  styleUrls: ['./cgu-modal.component.scss']
})
export class CguModalComponent {

  constructor(public dialogRef: MatDialogRef<CguModalComponent>) { }

  close(): void {
    this.dialogRef.close();
  }
}