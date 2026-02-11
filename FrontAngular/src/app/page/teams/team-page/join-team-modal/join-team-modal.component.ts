import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Team } from 'src/app/models/team.model';
import { TeamService } from 'src/app/service/team.service';
import { UserActions } from "../../../../store/user/user.actions";
import { Store } from '@ngrx/store';
import { TeamFacade } from 'src/app/store/team/team.facade';
import { User } from 'src/app/models/user.model';
import { NavigationStart, Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-join-team-modal',
  templateUrl: './join-team-modal.component.html',
  styleUrls: ['./join-team-modal.component.scss']
})
export class JoinTeamModalComponent {
  password: string = '';
  showErrorMessage: boolean = false;
  private routerSubscription: Subscription;
  
  constructor(
    public dialogRef: MatDialogRef<JoinTeamModalComponent>,
    private teamService: TeamService,
    @Inject(MAT_DIALOG_DATA) public data: { visitedUserTeam: Team, currentUser: User },
    private store: Store,
    private teamFacade: TeamFacade,
    private router: Router,
  )
  {
    this.routerSubscription = this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.dialogRef.close();
      }
    });
  }

  submitTeamPassword() {
    this.teamService.joinTeam(this.password, this.data.visitedUserTeam).subscribe({
      next: (response: Team) => {
        if (response && this.data.currentUser) {
          this.store.dispatch(UserActions.setHasOneTeam({ hasOneTeam: true }));
          this.teamFacade.addUserToTeam(response)
          this.router.navigate(['/']); 
          setTimeout(() => this.router.navigate(['/team', this.data.visitedUserTeam.name]));
          this.dialogRef.close();
        } else {
          this.showErrorMessage = true; 
        }
      },
      error: (error: any) => {
        this.showErrorMessage = true;
      }
    });
  }

  ngOnDestroy(): void {
    // Unsubscribe from the router events
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

}
