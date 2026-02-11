import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute } from '@angular/router';
import { Store, select } from '@ngrx/store';
import { Team } from 'src/app/models/team.model';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/service/auth/auth.service';
import { TeamService } from 'src/app/service/team.service';
import { selectVisitedTeam } from '../../../store/team/team.selectors';
import { Observable, Subject, combineLatest, map, takeUntil } from 'rxjs';
import { TeamActions } from '../../../store/team/team.actions'; 
import { TeamFacade } from 'src/app/store/team/team.facade';
import { Role } from 'src/app/models/role.enum';
import { MatDialog } from '@angular/material/dialog';
import { TeamFormComponent } from '../team-form/team-form.component';
import { selectCurrentUserTeam } from '../../../store/team/team.selectors';
import { DeleteTeamModalComponent } from './delete-team-modal/delete-team-modal.component';
import { JoinTeamModalComponent } from './join-team-modal/join-team-modal.component';
import { UserActions } from "../../../store/user/user.actions";
import { AlertState } from 'src/app/store/alert/alert.reducer';
import { AlertFacade } from 'src/app/store/alert/alert.facade';

@Component({
  selector: 'app-team-page',
  templateUrl: './team-page.component.html',
  styleUrls: ['./team-page.component.scss']
})
export class TeamPageComponent implements OnInit, OnDestroy {
  user$!: User | null;
  user!: User | null;
  displayedColumns: string[] = ['logo', 'name', 'mmr', 'country', 'actions'];
  dataSource = new MatTableDataSource<User>();
  visitedTeam$!: Observable<Team | null>;
  visitedTeam!: Team | null;
  private unsubscribe$ = new Subject<void>();
  isVisitedTeamAdmin: boolean = false;
  isVisitedTeamPlayer: boolean = false;
  isAuthenticated$: boolean = false;
  hasOneTeam$: boolean = false;
  decryptedPassword: string = '';
  currentUserTeam!: Team | null;
  alert$: Observable<AlertState>;

  constructor(
    private route: ActivatedRoute,
    private teamService: TeamService,
    private authService: AuthService,
    private store: Store,
    private teamFacade: TeamFacade,
    private dialog: MatDialog,
    private alertFacade: AlertFacade
  ) { 
    this.alert$ = this.alertFacade.alert$;
  }

  ngOnInit(): void {
    this.authService.isAuthenticated$.subscribe(isAuthenticated => {
      this.isAuthenticated$ = isAuthenticated;
    });
    this.store.select(selectCurrentUserTeam).subscribe(team => {
      this.currentUserTeam = team;
    });
    this.authService.hasOneTeam$.subscribe(hasOneTeam => {
      this.hasOneTeam$ = hasOneTeam;
    });

    this.getTeamWithUrl();
    this.visitedTeam$ = this.store.pipe(select(selectVisitedTeam));

    this.visitedTeam$.pipe(
      takeUntil(this.unsubscribe$)
    ).subscribe(team => {
      if (team) {
        this.isTeamAdmin(team.id).subscribe(isAdmin => {
          this.isVisitedTeamAdmin = isAdmin;
          if (isAdmin) {
            this.getTeamPassword();
          }
        }, error => {
          console.error(error);
        });

        this.isTeamPlayer(team.id).subscribe(isPlayer => {
          this.isVisitedTeamPlayer = isPlayer;
        }, error => {
          console.error(error);
        });
      } 
    });

    combineLatest([this.visitedTeam$, this.authService.user$]).pipe(
      takeUntil(this.unsubscribe$)
    ).subscribe(([team, user]) => {
      this.visitedTeam = team;
      this.user$ = user;
      this.user = user;
      if (team && team.users) {
        const sortedUsers = [...team.users].sort((a, b) => a.userOrder - b.userOrder);
        this.dataSource.data = sortedUsers;
      }
    });
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  getTeamWithUrl() {
    this.route.paramMap.subscribe(params => {
      const teamName = params.get('teamName');
      if (teamName != null) {
        this.store.dispatch(TeamActions.GetTeamDetails({ teamName }));
      }
    });
  }

  isKickable(steamId?: String): boolean {
    if (steamId === this.user$?.steamId64bits) {
      return false;
    }
    return true;
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

  isCaptain(user: User): boolean {
    return !!this.visitedTeam && !!this.visitedTeam.captain && this.visitedTeam.captain.id === user.id;
  }

  isPlayer(roles: Role[]): boolean {
    return roles.includes(Role.ROLE_PLAYER);
  }

  kickPlayer(user: User): void{
    this.teamService.kickPlayer(user.steamId64bits).subscribe({
      next: (response: Team) => {
        if (response) {
          this.teamFacade.removeUserFromTeam(response);
        } else {
          console.log('Failed to leave team.');
          // ERROR HANDLING FRONT à faire
        }
      },
      error: (error: any) => {
        console.error('Error leaving team:', error);
        // ERROR HANDLING FRONT à faire
      }
    });
  }
  
  openEditModal(mode: 'create' | 'edit', decryptedPassword: string): void {
    this.dialog.open(TeamFormComponent, {
    data: { mode: mode, team: this.visitedTeam, decryptedPassword: decryptedPassword }
  });
  }

  openDeleteModal(): void {
    this.dialog.open(DeleteTeamModalComponent, {
      data: { currentUserTeam: this.currentUserTeam}
    });
  }

  openJoinTeamModal(): void {
    this.dialog.open(JoinTeamModalComponent, {
      data: { visitedUserTeam: this.visitedTeam, currentUser: this.user }
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

  getMedalName(rankTier: string | number): string {
    const medalBaseNames: { [key: string]: string } = {
      '1': 'Herald',
      '2': 'Guardian',
      '3': 'Crusader',
      '4': 'Archon',
      '5': 'Legend',
      '6': 'Ancient',
      '7': 'Divine',
      '8': 'Immortal'
    };
  
    const rankTierStr = String(rankTier);
  
    if (!rankTierStr) {
      console.error('Invalid rankTier:', rankTier);
      return 'Unknown';
    }
  
    const baseRank = rankTierStr.charAt(0);
  
    if (!medalBaseNames[baseRank]) {
      console.error('No medal mapping found for baseRank:', baseRank);
      return 'Unknown';
    }
  
    return medalBaseNames[baseRank];
  }

  hasCountryData(): boolean {
    return this.dataSource.data.some((element: User) => !!element.country);
  }
  
}
