import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {
  LobbySauvageState,
  selectAllLobbySauvages,
  selectCurrentLobby
} from "../../../store/lobby-sauvage/lobby-sauvage.reducer";
import {select, Store} from "@ngrx/store";
import {LobbySauvageActions} from "../../../store/lobby-sauvage/lobby-sauvage.actions";
import {LobbySauvage} from "../../../store/lobby-sauvage/lobby-sauvage.model";
import {EMPTY, Observable, tap} from "rxjs";
import {User} from "../../../models/user.model";
import {selectIsAuthenticated} from "../../../store/auth/auth.selectors";
import {environment} from "../../../../environment/environment";
import { SteamAuthService } from 'src/app/service/auth/steam-auth.service';

@Component({
  selector: 'app-lobby-sauvage',
  templateUrl: './lobby-sauvage.component.html',
  styleUrls: ['./lobby-sauvage.component.scss']
})

export class LobbySauvageComponent implements OnInit, OnDestroy {
  eventSource!: EventSource;
  imageUrl = "assets/img/almostsauvage320180.png"
  isAuthenticated$: Observable<boolean>;
  isAuthenticated: boolean;
  lobbies: LobbySauvage[] = [];
  isAuth: boolean = false;
  
  constructor(private lobbySauvageStore: Store<LobbySauvageState>, private store: Store, private cd: ChangeDetectorRef, private steamAuthService: SteamAuthService) {
    this.lobbies = [];
    this.isAuthenticated$ = EMPTY;
    this.isAuthenticated = false;
  }

  ngOnInit() {
    this.connectToServer()

    this.lobbySauvageStore.dispatch(LobbySauvageActions.getCurrentLobby());
    this.lobbySauvageStore.select(selectCurrentLobby()).pipe(
      tap( currentLobby => {
        if (currentLobby) {
          this.lobbySauvageStore.dispatch(LobbySauvageActions.joinLobbySuccess({lobby: currentLobby}));
        }
      })
    ).subscribe();
    this.isAuthenticated$ = this.store.pipe(select(selectIsAuthenticated));
    this.isAuthenticated$.pipe(
      tap(isAuthenticated => this.isAuth = isAuthenticated)
    ).subscribe();

    this.store.pipe(select(selectIsAuthenticated)).pipe(
      tap(it => {
        if (it != undefined) {
          this.isAuthenticated = it;
        }
      })
    ).subscribe();
    this.lobbySauvageStore.dispatch(LobbySauvageActions.getAllLobbySauvage());
    this.lobbySauvageStore.select(selectAllLobbySauvages).pipe(
      tap(it => {
        if (it) {
          this.lobbies = [...it];
          this.cd.detectChanges();
        }
      })
    ).subscribe();
  }

  private connectToServer() {
    const options: { withCredentials: boolean } = {
      withCredentials: true 
    };
    
    this.eventSource = new EventSource(`${environment.backendUrl}/sse/lobby-sauvage-list-event`, options);

    this.eventSource.onmessage = (event) => {
      console.log('New data:', event.data);
      this.lobbySauvageStore.dispatch(LobbySauvageActions.getAllLobbySauvage());
    };

    this.eventSource.addEventListener('INIT', (event) => {
      console.log('Connected:', event.data);
    });
  }
  

  tryToCreateLobby() {
    if (this.isAuth) {
      this.lobbySauvageStore.dispatch(LobbySauvageActions.createLobbySauvage())
    } else {
      this.steamAuthService.processSteamAuth();
    }
  }

  getUsersNamesList(users: User[]) {
    return users.map(us => us.userName).join(', ');
  }

  joinLobby(lobby: LobbySauvage) {
    console.log('JOIN LOBBY : ' + lobby.id);
    this.lobbySauvageStore.dispatch(LobbySauvageActions.joinLobby({lobby: lobby}))
  }

  ngOnDestroy() {
    this.eventSource.close();
  }
}
