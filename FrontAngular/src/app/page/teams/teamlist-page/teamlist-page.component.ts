import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Observable, Subject, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, map, startWith, switchMap, takeUntil } from 'rxjs/operators';
import { Team } from 'src/app/models/team.model';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from 'src/app/service/auth/auth.service';
import { Router } from '@angular/router';
import { TeamFormComponent } from '../team-form/team-form.component';
import { TeamFacade } from 'src/app/store/team/team.facade';
import { SteamAuthService } from 'src/app/service/auth/steam-auth.service';
import { Store } from '@ngrx/store';
import { selectCurrentUserTeam } from 'src/app/store/team/team.selectors';
import { TeamActions } from "../../../store/team/team.actions";
import { environment } from 'src/environment/environment';
import { TeamState } from 'src/app/store/team/team.reducer';

const backendUrl = environment.backendUrl;

@Component({
  selector: 'app-teamlist-page',
  templateUrl: './teamlist-page.component.html',
  styleUrls: ['./teamlist-page.component.scss']
})
  
export class TeamlistPageComponent implements OnInit, AfterViewInit, OnDestroy {
  displayedColumns: string[] = ['logo', 'name', 'captain', 'createdAt'];
  dataSource = new MatTableDataSource<Team>();
  hasOneTeam$: boolean = false;
  isAuthenticated$: boolean = false;
  isAuthenticated: boolean = false;
  teams$!: Observable<Team[]>;
  private unsubscribe$ = new Subject<void>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  searchTerms = new Subject<string>();
  currentUserTeam!: Team | null;
  private eventSourceTeam: EventSource | null = null;

  constructor(
    private dialogRef: MatDialog,
    private authService: AuthService,
    private router: Router,
    private teamFacade: TeamFacade,
    private steamAuthService: SteamAuthService,
    private store: Store,
    private teamStore: Store<TeamState>,
    private cd: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.initTeamListEventSource();

    this.store.select(selectCurrentUserTeam).pipe(
      takeUntil(this.unsubscribe$) 
    ).subscribe(team => {
      this.currentUserTeam = team;

      this.authService.isAuthenticated$.pipe(
        takeUntil(this.unsubscribe$) 
      ).subscribe((isAuthenticated: boolean) => {
        this.isAuthenticated$ = isAuthenticated;
        this.isAuthenticated = isAuthenticated;

        this.teams$ = this.searchTerms.pipe(
          startWith(''),
          debounceTime(100),
          distinctUntilChanged(),
          switchMap((term: string) => {
            if (term.trim() === '') {
              return this.teamFacade.getAllTeams().pipe(
                map(teams => this.sortTeamsWithCurrentUserTeamFirst(teams))
              );
            } else {
              return this.teamFacade.searchTeams(term);
            }
          })
        );

        this.teams$.pipe(
          takeUntil(this.unsubscribe$)
        ).subscribe(teams => {
          this.updateDataSource(teams);
          this.cd.detectChanges();
        });
      });
    });

    this.authService.hasOneTeam$.pipe(
      takeUntil(this.unsubscribe$)
    ).subscribe((hasOneTeam: boolean) => {
      this.hasOneTeam$ = hasOneTeam;
    });
  }
  
  private updateDataSource(teams: Team[]): void {
    this.dataSource.data = teams.map(team => ({
      ...team,
      createdAt: team.createdAt ? this.formatDate(team.createdAt) : null
    }));
  }

  private sortTeamsWithCurrentUserTeamFirst(teams: Team[]): Team[] {
    if (!this.currentUserTeam) {
      return teams;
    }
    return [this.currentUserTeam, ...teams.filter(team => team.id !== this.currentUserTeam?.id)];
  }
  
  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  ngOnDestroy(): void {
    if (this.eventSourceTeam) {
      this.eventSourceTeam.close();
    }
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  private initTeamListEventSource(): void {
    const backendUrl = environment.backendUrl;
    const options: { withCredentials: boolean } = {
      withCredentials: true 
    };

    this.eventSourceTeam = new EventSource(`${backendUrl}/sse/team-list-event`, options);

    this.eventSourceTeam.addEventListener('INIT', (event) => {
      this.teamStore.dispatch(TeamActions.GetAllTeams());
    });

    this.eventSourceTeam.onmessage = (event) => {
      console.log('New data:', event.data);
      this.teamStore.dispatch(TeamActions.GetAllTeams());
      this.cd.detectChanges();
    };
  }
  
  formatDate(date: Date | string | null): string | null {
    if (!date) {
      return null;
    }
  
    if (typeof date === 'string') {
      const parsedDate = new Date(date);
      if (!isNaN(parsedDate.getTime())) {
        const day = parsedDate.getDate().toString().padStart(2, '0');
        const month = (parsedDate.getMonth() + 1).toString().padStart(2, '0');
        const year = parsedDate.getFullYear();
        return `${day} / ${month} / ${year}`;
      } else {
        return null;
      }
    } else if (typeof date === 'object' && date instanceof Date) {
      const day = date.getDate().toString().padStart(2, '0');
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const year = date.getFullYear();
      return `${day} / ${month} / ${year}`;
    } else {
      return null;
    }
  }

  search(term: string): void {
    this.searchTerms.next(term);
  }

  openTeamForm(mode: 'create' | 'edit'): void {
    if (this.isAuthenticated) {
      this.dialogRef.open(TeamFormComponent, {
        data: { mode: mode }
      });
    } else {
      this.steamAuthService.processSteamAuth();
    }
  }

  redirectToTeamPage(team: Team) {
    this.teamFacade.setVisitedTeam(team);
    this.router.navigate(['/team', team.name]); 
  }
}
