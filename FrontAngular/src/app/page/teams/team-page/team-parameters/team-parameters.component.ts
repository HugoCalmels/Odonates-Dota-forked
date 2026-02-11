import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Team } from 'src/app/models/team.model';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/service/auth/auth.service';
import { TeamFormComponent } from '../../team-form/team-form.component';
import { MatDialog } from '@angular/material/dialog';
import { TeamService } from 'src/app/service/team.service';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { TeamActions } from '../../../../store/team/team.actions';
import { UserActions } from "../../../../store/user/user.actions";
import { selectVisitedTeam } from '../../../../store/team/team.selectors';
import { selectCurrentUserTeam } from '../../../../store/team/team.selectors';
import { Observable, Subject, map, takeUntil } from 'rxjs';
import { TeamFacade } from 'src/app/store/team/team.facade';

@Component({
  selector: 'app-team-parameters',
  templateUrl: './team-parameters.component.html',
  styleUrls: ['./team-parameters.component.scss']
})
  
export class TeamParametersComponent implements OnInit, OnDestroy {
  hasOneTeam$: boolean = false;
  isAuthenticated$: boolean = false;
  user!: User | null;
  scrimsPlayed: number | null = null;
  decryptedPassword: string = '';
  password: string = '';
  currentUserTeam!: Team | null;
  visitedTeam!: Team | null;
  showErrorMessage: boolean = false;
  private destroy$ = new Subject<void>();
  @Input() isVisitedTeamAdmin: boolean = false;
  @Input() isVisitedTeamPlayer: boolean = false;

  constructor(
    private authService: AuthService,
    private dialog: MatDialog,
    private teamService: TeamService,
    private router: Router,
    private store: Store,
    private teamFacade: TeamFacade
  ) { }
  
  ngOnInit(): void {
    this.store.select(selectCurrentUserTeam)
      .pipe(takeUntil(this.destroy$))
      .subscribe(team => this.currentUserTeam = team);

    this.store.select(selectVisitedTeam)
      .pipe(takeUntil(this.destroy$))
      .subscribe(team => this.visitedTeam = team);

    this.authService.hasOneTeam$
      .pipe(takeUntil(this.destroy$))
      .subscribe(hasOneTeam => this.hasOneTeam$ = hasOneTeam);

    this.authService.isAuthenticated$
      .pipe(takeUntil(this.destroy$))
      .subscribe(isAuthenticated => {
        this.isAuthenticated$ = isAuthenticated;
      });

    this.authService.user$
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => {
        this.user = user;
      });
      this.getScrimsPlayed();
  }

  ngOnChanges(): void {
    if (this.isVisitedTeamAdmin || this.isVisitedTeamPlayer) {
      this.getTeamPassword();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  openDialog(mode: 'create' | 'edit', decryptedPassword: string): void {
    this.dialog.open(TeamFormComponent, {
      data: { mode: mode, team: this.visitedTeam, decryptedPassword: decryptedPassword }
    });
  }

  submitTeamPassword() {
    this.teamService.joinTeam(this.password, this.visitedTeam).subscribe({
      next: (response: Team) => {
        if (response && this.user) {
          this.store.dispatch(UserActions.setHasOneTeam({ hasOneTeam: true }));
          this.teamFacade.addUserToTeam(response)
          this.router.navigate(['/']); 
          setTimeout(() => this.router.navigate(['/team', this.visitedTeam?.name]));
        } else {
          this.showErrorMessage = true; 
        }
      },
      error: (error: any) => {
        this.showErrorMessage = true;
      }
    });
  }

  getTeamPassword() {
    this.teamService.getTeamPassword().subscribe({
        next: (password: string) => {
            this.decryptedPassword = password; 
        },
        error: (error: any) => {
            console.error('Error getting team password:', error);
            // ERROR HANDLING FRONT à faire
        }
    });
  }

  getScrimsPlayed() {
    this.teamService.getNumberScrimsPlayed(this.visitedTeam?.id).subscribe({
      next: (numberOfScrimsPlayed: number) => {
        this.scrimsPlayed = numberOfScrimsPlayed;
        },
        error: (error: any) => {
            console.error('Error', error);
            // ERROR HANDLING FRONT à faire
        }
    });
  }
  
  leaveTeam(): void {
    this.teamService.leaveTeam().subscribe({
      next: (response: Team) => {
        if (response && this.user) {
          this.teamFacade.removeUserFromTeam(response);
          this.store.dispatch(UserActions.setHasOneTeam({ hasOneTeam: false }));
        } else {
          // ERROR HANDLING FRONT à faire
        }
      },
      error: (error: any) => {
        // ERROR HANDLING FRONT à faire
      }
    });
  }

  isTeamAdmin(teamId: string | null): Observable<boolean> {
    return this.teamService.isUserTeamAdmin(teamId).pipe(
      map(isAdmin => {
        if (isAdmin) {
          return true;
        }
        return false;
      })
    );
  }

  isTeamPlayer(teamId: string | null): Observable<boolean> {
    return this.teamService.isUserTeamPlayer(teamId).pipe(
      map(isPlayer => {
        if (isPlayer) {
          return true;
        }
        return false;
      })
    );
  }

  deleteTeam(): void {
    this.teamService.deleteTeam().subscribe({
      next: () => {
        if (this.currentUserTeam && this.currentUserTeam.id) {
          this.store.dispatch(UserActions.setHasOneTeam({ hasOneTeam: false }));
          this.store.dispatch(TeamActions.DeleteTeam({ teamId: this.currentUserTeam.id }));
          this.router.navigate(['/teams']); 
        }
      },
      error: (error) => {
        // ERROR HANDLING FRONT à faire
      }
    });
  }

}
