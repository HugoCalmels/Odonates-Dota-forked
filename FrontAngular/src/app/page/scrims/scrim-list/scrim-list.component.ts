import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Scrim } from 'src/app/models/scrim/scrim.model';
import { Observable, Subject, takeUntil, tap } from 'rxjs';
import { ScrimFacade } from 'src/app/store/scrim/scrim.facade';
import { Router } from '@angular/router';
import * as moment from 'moment-timezone';
import { PreGameStatus } from 'src/app/models/scrim/enums/preGameStatus.enum';
import { MmrAveragePipe } from 'src/app/utils/mmr-average.pipe';
import { User } from 'src/app/models/user.model';
import { GameMode } from 'src/app/models/scrim/enums/gameMode.enum';
import { environment } from 'src/environment/environment';
import { Store } from '@ngrx/store';
import { ScrimState } from 'src/app/store/scrim/scrim.reducer';
import { loadScrimList } from "../../../store/scrim/scrim.actions";

const backendUrl = environment.backendUrl;

@Component({
  selector: 'app-scrim-list',
  templateUrl: './scrim-list.component.html',
  styleUrls: ['./scrim-list.component.scss']
})
export class ScrimListComponent implements OnInit, AfterViewInit, OnDestroy {
  scrimList$!: Observable<Scrim[]>;
  displayedColumns: string[] = ['startDateTime', 'gameMode', 'firstScrimTeam', 'secondScrimTeam', 'preGameStatus'];
  dataSource = new MatTableDataSource<Scrim>();
  originalScrimList: Scrim[] = [];
  userTimeZone: string;
  @ViewChild(MatSort) sort!: MatSort;
  reverseStartDateTimeSort: boolean = false;
  gameModesOrder = [
    GameMode.MID_ONLY_1V1,
    GameMode.MID_ONLY_2V2,
    GameMode.ALL_PICK_5V5,
    GameMode.CAPTAINS_MODE_5V5,
    GameMode.RANDOM_DRAFT_5V5
  ];
  currentGameModeIndex = 0;
  sortDirection: 'asc' | 'desc' = 'asc';
  currentSortColumn: string = '';
  eventSource!: EventSource;
  private unsubscribe$ = new Subject<void>();

  constructor(
    private scrimFacade: ScrimFacade,
    private router: Router,
    private mmrAveragePipe: MmrAveragePipe,
    private scrimStore: Store<ScrimState>,
    private cd: ChangeDetectorRef
  ) {
    this.userTimeZone = moment.tz.guess();
  }

  ngOnInit(): void {
    this.initScrimListEventSource();
    this.loadScrimList();

    this.scrimList$.pipe(
      tap(scrims => {
        this.dataSource.data = scrims;
        this.originalScrimList = [...scrims];
        this.cd.detectChanges();
      }),
      takeUntil(this.unsubscribe$)
    ).subscribe();
  }

  ngAfterViewInit(): void {
    this.dataSource.sortingDataAccessor = (item: Scrim, property: string): string | number => {
      switch (property) {
        case 'startDateTime':
          return item.startDateTime ? new Date(item.startDateTime).getTime() : 0;
        default:
          return (item as any)[property];
      }
    };
  }

  ngOnDestroy(): void {
    if (this.eventSource) {
      this.eventSource.close();
    }
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  private initScrimListEventSource(): void {
    const backendUrl = environment.backendUrl;
    const options: { withCredentials: boolean } = {
        withCredentials: true
    };
 
    this.eventSource = new EventSource(`${backendUrl}/sse/scrim-list-event`, options);

    this.eventSource.addEventListener('INIT', (event) => {
      this.scrimStore.dispatch(loadScrimList({ timezone: this.userTimeZone }));
    });
    this.eventSource.onmessage = (event) => {
      console.log('New data:', event.data);
      this.scrimStore.dispatch(loadScrimList({ timezone: this.userTimeZone }));
      this.cd.detectChanges();
    };
  }

  loadScrimList(): void {
    this.scrimFacade.loadScrimList(this.userTimeZone);
    this.scrimList$ = this.scrimFacade.scrimList$;
  }

  navigateToScrimDetail(scrim: Scrim) {
    this.router.navigate(['/scrim/', scrim.id]);
  }

  isJoinable(scrim: Scrim) {
    return scrim.preGameStatus === PreGameStatus.JOINABLE
  }

  sortData(event: Sort): void {
    const sortColumn = event.active;
  
    if (!sortColumn) {
      return;
    }
  
    this.updateSortDirection(sortColumn);
    let sortedData: Scrim[] = [...this.dataSource.data];
  
    switch (sortColumn) {
      case 'firstScrimTeam':
        sortedData = this.sortByTeam(sortedData, 'firstScrimTeam');
        break;
      case 'secondScrimTeam':
        sortedData = this.sortByTeam(sortedData, 'secondScrimTeam');
        break;
      case 'startDateTime':
        sortedData = this.sortByStartDateTime();
        break;
      case 'gameMode':
        sortedData = this.sortByGameMode(sortedData);
        break;
      case 'preGameStatus':
        sortedData = this.sortByPreGameStatus(sortedData);
        break;
    }
    this.dataSource.data = sortedData;
    this.dataSource._updateChangeSubscription();
  }

  private updateSortDirection(sortColumn: string): void {
    if (sortColumn === this.currentSortColumn) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortDirection = 'asc';
    }
    this.currentSortColumn = sortColumn;
  }
  
  private sortByTeam(data: Scrim[], team: 'firstScrimTeam' | 'secondScrimTeam'): Scrim[] {
    return data.sort((a, b) => {
      const teamA = a[team];
      const teamB = b[team];
      
      const mmrA = teamA && teamA.users ? this.mmrAveragePipe.transform(teamA.users) : 0;
      const mmrB = teamB && teamB.users ? this.mmrAveragePipe.transform(teamB.users) : 0;
      
      return this.sortDirection === 'asc' ? mmrA - mmrB : mmrB - mmrA;
    });
  }
  
  private sortByStartDateTime(): Scrim[] {
    let sortedData = [...this.originalScrimList];
    if (this.reverseStartDateTimeSort) {
      sortedData.reverse();
    }
    this.reverseStartDateTimeSort = !this.reverseStartDateTimeSort;
    return sortedData;
  }
  
  private sortByGameMode(data: Scrim[]): Scrim[] {
    data.sort((a, b) => {
      const orderA = this.gameModesOrder.indexOf(a.gameMode);
      const orderB = this.gameModesOrder.indexOf(b.gameMode);
      if (orderA === this.currentGameModeIndex) return -1;
      if (orderB === this.currentGameModeIndex) return 1;
      return orderA - orderB;
    });
    this.currentGameModeIndex = (this.currentGameModeIndex + 1) % this.gameModesOrder.length;
    return data;
  }
  
  private sortByPreGameStatus(data: Scrim[]): Scrim[] {
    const statusOrder = {
      [PreGameStatus.JOINABLE]: 1,
      [PreGameStatus.FULL]: 2
    };
    
    return data.sort((a, b) => {
      const orderA = a.preGameStatus ? statusOrder[a.preGameStatus] : 0;
      const orderB = b.preGameStatus ? statusOrder[b.preGameStatus] : 0;
      return this.sortDirection === 'asc' ? orderA - orderB : orderB - orderA;
    });
  }

  addCaptainToUsers(users: User[], captain: User): User[] {
    return captain ? [captain, ...users] : users;
  }

  compareValues(valueA: any, valueB: any): number {
    if (valueA < valueB) {
      return -1;
    } else if (valueA > valueB) {
      return 1;
    } else {
      return 0;
    }
  }

}
