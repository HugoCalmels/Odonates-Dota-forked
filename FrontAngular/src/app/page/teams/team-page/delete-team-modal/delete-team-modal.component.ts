import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TeamService } from 'src/app/service/team.service';
import { TeamActions } from '../../../../store/team/team.actions';
import { UserActions } from "../../../../store/user/user.actions";
import { Store } from '@ngrx/store';
import { NavigationStart, Router } from '@angular/router';
import { Team } from 'src/app/models/team.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-delete-team-modal',
  templateUrl: './delete-team-modal.component.html',
  styleUrls: ['./delete-team-modal.component.scss']
})
export class DeleteTeamModalComponent {
  private routerSubscription: Subscription;

  constructor(
    public dialogRef: MatDialogRef<DeleteTeamModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { currentUserTeam: Team },
    private teamService: TeamService,
    private store: Store,
    private router: Router
  ) {
    this.routerSubscription = this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.dialogRef.close();
      }
    });
   }
  
  deleteTeam(): void {
    this.teamService.deleteTeam().subscribe({
      next: () => {
        if (this.data.currentUserTeam && this.data.currentUserTeam.id) {
          this.dialogRef.close();
          this.store.dispatch(UserActions.setHasOneTeam({ hasOneTeam: false }));
          this.store.dispatch(TeamActions.DeleteTeam({ teamId: this.data.currentUserTeam.id }));
          this.router.navigate(['/teams']); 
        }
      },
      error: (error) => {
        // ERROR HANDLING FRONT à faire
      }
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  ngOnDestroy(): void {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

}
