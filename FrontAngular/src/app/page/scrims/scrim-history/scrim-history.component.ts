import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { Observable, of } from 'rxjs';
import { GameMode } from 'src/app/models/scrim/enums/gameMode.enum';
import { GameStatus } from 'src/app/models/scrim/enums/gameStatus.enum';
import { Scrim } from 'src/app/models/scrim/scrim.model';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/service/auth/auth.service';
import { ScrimFacade } from 'src/app/store/scrim/scrim.facade';
import { MmrAveragePipe } from 'src/app/utils/mmr-average.pipe';

@Component({
  selector: 'app-scrim-history',
  templateUrl: './scrim-history.component.html',
  styleUrls: ['./scrim-history.component.scss']
})
export class ScrimHistoryComponent implements OnInit, AfterViewInit {
  scrimList$!: Observable<Scrim[]>;
  displayedColumns: string[] = ['startDateTime', 'gameMode', 'firstScrimTeam', 'secondScrimTeam', 'gameStatus'];
  dataSource = new MatTableDataSource<Scrim>();
  originalScrimList: Scrim[] = []; 
  userTimeZone: string;
  @ViewChild(MatSort) sort!: MatSort;
  reverseStartDateTimeSort: boolean = false; 
  isAuthenticated: boolean = false;
  gameModesOrder = [
    GameMode.MID_ONLY_1V1,
    GameMode.MID_ONLY_2V2,
    GameMode.ALL_PICK_5V5,
    GameMode.CAPTAINS_MODE_5V5,
    GameMode.RANDOM_DRAFT_5V5
  ];
  gameStatusOrder = [
    GameStatus.NOT_STARTED,
    GameStatus.READY,
    GameStatus.STARTED,
    GameStatus.FINISHED
  ];
  currentGameModeIndex = 0;
  currentGameStatusIndex = 0;
  sortDirection: 'asc' | 'desc' = 'asc';
  currentSortColumn: string = '';

  constructor(
    private scrimFacade: ScrimFacade,
    private router: Router,
    private mmrAveragePipe: MmrAveragePipe,
    private authService: AuthService
  ) {
    this.userTimeZone = moment.tz.guess();
  }

  ngOnInit(): void {
    this.authService.isAuthenticated$.subscribe((isAuthenticated: boolean) => {
      this.isAuthenticated = isAuthenticated;
      this.loadScrimList();
      this.scrimList$.subscribe(scrims => {
        this.dataSource.data = scrims;
        this.originalScrimList = [...scrims];
      });
    });
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

  loadScrimList(): void {
    if (this.isAuthenticated) {
      this.scrimFacade.loadScrimHistory(this.userTimeZone);
      this.scrimList$ = this.scrimFacade.scrimList$;
    } else {
      this.scrimList$ = of([]);
    }
  }

  navigateToScrimDetail(scrim: Scrim) {
    this.router.navigate(['/scrim/', scrim.id]);
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
      case 'gameStatus':
        sortedData = this.sortByGameStatus(sortedData);
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

  private sortByGameStatus(data: Scrim[]): Scrim[] {
    data.sort((a, b) => {
      const orderA = a.gameStatus ? this.gameStatusOrder.indexOf(a.gameStatus) : -1;
      const orderB = b.gameStatus ? this.gameStatusOrder.indexOf(b.gameStatus) : -1;
  
      if (orderA === -1) return 1; 
      if (orderB === -1) return -1; 
  
      if (orderA === this.currentGameStatusIndex) return -1;
      if (orderB === this.currentGameStatusIndex) return 1;
  
      return orderA - orderB;
    });
    this.currentGameStatusIndex = (this.currentGameStatusIndex + 1) % this.gameStatusOrder.length;
    return data;
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
