import { Component, Inject} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NavigationStart, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ScrimFacade } from 'src/app/store/scrim/scrim.facade';

@Component({
  selector: 'app-delete-scrim-modal',
  templateUrl: './delete-scrim-modal.component.html',
  styleUrls: ['./delete-scrim-modal.component.scss']
})
export class DeleteScrimModalComponent {
  private routerSubscription: Subscription;

  constructor(
    public dialogRef: MatDialogRef<DeleteScrimModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { scrimId: number },
    private scrimFacade: ScrimFacade,
    private router: Router
  ) {
    this.routerSubscription = this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.dialogRef.close();
      }
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onDelete(): void {
    this.scrimFacade.deleteScrim(this.data.scrimId);
    this.dialogRef.close('Scrim deleted');
  }
}