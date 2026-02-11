import {Component, OnDestroy, OnInit} from '@angular/core';
import {LobbySauvageState, selectCurrentLobby} from "../../../store/lobby-sauvage/lobby-sauvage.reducer";
import {Store} from "@ngrx/store";
import {tap} from "rxjs";
import {LobbySauvage} from "../../../store/lobby-sauvage/lobby-sauvage.model";
import {LobbySauvageActions} from "../../../store/lobby-sauvage/lobby-sauvage.actions";
import {WebsocketService} from "../../../service/websocket-service";

@Component({
  selector: 'app-lobby-sauvage-details',
  templateUrl: './lobby-sauvage-details.component.html',
  styleUrls: ['./lobby-sauvage-details.component.scss']
})
export class LobbySauvageDetailsComponent implements OnInit {
  lobby: LobbySauvage | undefined;
  constructor(private websocketService: WebsocketService,
              private lobbyStore: Store<LobbySauvageState>) {
    this.lobby = undefined;
    this.websocketService.activate();
  }

  ngOnInit() {
    //TODO check if user is in the lobby serverside
    this.lobbyStore.dispatch(LobbySauvageActions.getCurrentLobby());
    this.lobbyStore.select(selectCurrentLobby()).pipe(
      tap(lobby => {
        if (lobby) {
          this.lobby = lobby;
          this.websocketService.subscribeToRoom(this.lobby.id.toString(), message => {
            this.lobbyStore.dispatch(LobbySauvageActions.getCurrentLobby());
          })
        }
      })
    ).subscribe()
  }

  leaveTheLobby() {
    this.lobbyStore.dispatch(LobbySauvageActions.leaveLobby({lobby: this.lobby!}));
  }
}
