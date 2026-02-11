import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { PreGameStatus } from 'src/app/models/scrim/enums/preGameStatus.enum';
import { Scrim } from 'src/app/models/scrim/scrim.model';
import * as moment from 'moment-timezone';
import { GameStatus } from 'src/app/models/scrim/enums/gameStatus.enum';
import { ScrimDetailsResponse } from 'src/app/models/scrim/scrimDetailsResponse';
import { ScrimFacade } from 'src/app/store/scrim/scrim.facade';
import { Observable, Subscription, combineLatest, distinctUntilChanged, filter, map, of, switchMap } from 'rxjs';
import { ScrimProposal } from 'src/app/models/scrim/scrimProposal';
import { ProposalStatus } from 'src/app/models/scrim/enums/proposalStatus.enum';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/service/auth/auth.service';
import { PlayerSelectionModalComponent } from './player-selection-modal/player-selection-modal.component';
import { ProposalDetailsComponent } from './proposal-details/proposal-details.component';
import { DeleteScrimModalComponent } from './delete-scrim-modal/delete-scrim-modal.component';
import { SteamAuthService } from 'src/app/service/auth/steam-auth.service';
import { AlertFacade } from 'src/app/store/alert/alert.facade';
import { AlertState } from 'src/app/store/alert/alert.reducer';

@Component({
  selector: 'app-scrim-details',
  templateUrl: './scrim-details.component.html',
  styleUrls: ['./scrim-details.component.scss']
})
export class ScrimDetailsComponent implements OnInit, OnDestroy {
  displayedColumns: string[] = ['logo', 'playerName', 'MMR'];
  scrimId!: number;
  showAlert: boolean = false;
  userTimeZone: string;
  scrim$: Observable<Scrim | null>;
  userStatus$: Observable<ScrimDetailsResponse | null>;
  isTeamAdmin: boolean = false;
  user: User | null = null;
  isAuthenticated: boolean = false;
  private subscriptions: Subscription[] = [];
  alert$: Observable<AlertState>;
  GameStatus = GameStatus;

  constructor(
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private scrimFacade: ScrimFacade, 
    private authService: AuthService, 
    private steamAuthService: SteamAuthService,
    private alertFacade: AlertFacade
  ) {
    this.userTimeZone = moment.tz.guess();
    this.scrim$ = this.scrimFacade.scrim$;
    this.userStatus$ = this.scrimFacade.userStatus$;
    this.alert$ = this.alertFacade.alert$;
  }

  ngOnInit(): void {
    this.initializeScrimDetails();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }

  private initializeScrimDetails(): void {
    const routeSubscription = this.route.paramMap.pipe(
      switchMap(params => {
        const id = params.get('scrimId');
  
        if (id !== null) {
          this.scrimId = +id;
          this.scrimFacade.loadScrimDetails(this.scrimId, this.userTimeZone);
  
          return combineLatest([
            this.authService.isAuthenticated$.pipe(
              filter(isAuthenticated => isAuthenticated),
              switchMap(() => this.authService.user$)
            ),
            this.userStatus$.pipe(
              distinctUntilChanged((prev, curr) => JSON.stringify(prev) === JSON.stringify(curr)) 
            ),
            this.scrim$.pipe(
              filter(scrim => !!scrim),
              distinctUntilChanged((prev, curr) => JSON.stringify(prev) === JSON.stringify(curr))
            )
          ]);
        }
        return of([null, null, null]);
      })
    ).subscribe(
      ([user, status, scrim]) => {
        this.isAuthenticated = true;
        console.log('User Status:', status); 
        this.handleScrimDetails(user, status, scrim);
        this.scrimFacade.getScrimUserStatus(this.scrimId);
      },
      error => {
        console.error('Failed to retrieve scrim details:', error);
      }
    );
  
    this.subscriptions.push(routeSubscription);
  }

  private handleScrimDetails(user: User | null, status: ScrimDetailsResponse | null, scrim: Scrim | null): void {
    this.user = user;
    if (status != null) {
      this.isTeamAdmin = status.canDeleteScrim;
    }
  }

  cancelScrimProposal(): void {
    this.scrimFacade.cancelScrimProposal(this.scrimId, this.userTimeZone);
  }

  acceptProposal(proposal: ScrimProposal): void {
    this.scrimFacade.acceptScrimProposal(proposal.id, this.userTimeZone);
  }

  rejectProposal(proposal: ScrimProposal): void {
    this.scrimFacade.rejectScrimProposal(proposal.id, this.userTimeZone);
  }

  checkProposalStatus(proposal: ScrimProposal): boolean {
    return proposal.status === ProposalStatus.PENDING;
  }

  scrimIsFull(): Observable<boolean> {
    return this.scrim$.pipe(
      filter(scrim => !!scrim),
      map(scrim => scrim!.preGameStatus === PreGameStatus.FULL),
    );
  }

  openDeleteModal(scrimId: number) {
    this.dialog.open(DeleteScrimModalComponent, {
      data: { scrimId: scrimId }
    });
  }

  cancelScrim(): void {
    this.scrimFacade.cancelScrim(this.scrimId);
  }

  isNotStarted(scrim: Scrim): boolean {
    return scrim?.gameStatus === GameStatus.NOT_STARTED;
  }

  checkIfTeamAdmin(): boolean {
    return this.isTeamAdmin;
  }

  gameIsFull(scrim: Scrim): boolean {
    return scrim?.preGameStatus === PreGameStatus.FULL;
  }

  getGameStatusClass(gameStatus: GameStatus): string {
    switch (gameStatus) {
      case GameStatus.NOT_STARTED:
        return 'not-started';
      case GameStatus.STARTED:
        return 'started';
      default:
        return '';
    }
  }

  openPlayerSelectionModal(): void {
    this.dialog.open(PlayerSelectionModalComponent, {
      data: {
        users: this.user?.team?.users,
        scrim: this.scrim$
      }
    });
  }

  openProposalDetailsModal(proposal: ScrimProposal) {
    this.dialog.open(ProposalDetailsComponent, {
      data: { proposal: proposal }
    });
  }

  redirectToLogin(): void {
    this.steamAuthService.processSteamAuth();
  }
}