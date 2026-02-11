import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NavigationStart, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ScrimProposal } from 'src/app/models/scrim/scrimProposal';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-proposal-details',
  templateUrl: './proposal-details.component.html',
  styleUrls: ['./proposal-details.component.scss']
})
export class ProposalDetailsComponent implements OnInit {
  proposal!: ScrimProposal;
  displayedUsers: User[] = [];
  private routerSubscription: Subscription;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { proposal: ScrimProposal },
    private router: Router,
    private dialogRef: MatDialogRef<ProposalDetailsComponent>,
  ) {
    this.routerSubscription = this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.dialogRef.close();
      }
    });
   }

  ngOnInit(): void {
    this.proposal = this.data.proposal;
    this.sortUsers();
  }

  navigateToTeamDetail(teamName: string | null) {
    if (teamName) { 
      const teamUrl = `/team/${teamName}`;
      window.open(teamUrl, '_blank');
    } else {
      // Todo : redirect "oups this page dosnt exists"
      console.error('Team ID is null or undefined');
    }
  }

  private sortUsers(): void {
    if (this.proposal.proposerScrimTeam && this.proposal.proposerScrimTeam.users) {
      const captain = this.proposal.proposerScrimTeam.users.find(user => user.id === this.proposal.proposerScrimTeam.squadCaptain.id);
      const otherUsers = this.proposal.proposerScrimTeam.users.filter(user => user.id !== this.proposal.proposerScrimTeam.squadCaptain.id);
      this.displayedUsers = captain ? [captain, ...otherUsers] : this.proposal.proposerScrimTeam.users;
    }
  }

  isCaptain(user: User): boolean {
    return this.proposal.proposerScrimTeam && this.proposal.proposerScrimTeam.squadCaptain && user.id === this.proposal.proposerScrimTeam.squadCaptain.id;
  }
}