import {NgModule} from '@angular/core';
import {CommonModule,} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {RouterModule, Routes} from '@angular/router';
import {HomePageComponent} from "./page/home/home-page/home-page.component";
import {TeamPageComponent} from './page/teams/team-page/team-page.component';
import {TeamlistPageComponent} from './page/teams/teamlist-page/teamlist-page.component';
import {LeaderboardComponent} from "./page/lobby-sauvages/leaderboard/leaderboard.component";
import {LobbySauvageComponent} from "./page/lobby-sauvages/lobby-list/lobby-sauvage.component";
import {
  LobbySauvageDetailsComponent
} from "./page/lobby-sauvages/lobby-sauvage-details/lobby-sauvage-details.component";
import { ScrimsPageComponent } from './page/scrims/scrims-page/scrims-page.component';
import { ScrimDetailsComponent } from './page/scrims/scrim-details/scrim-details.component';
import { ScrimHistoryComponent } from './page/scrims/scrim-history/scrim-history.component';


const routes: Routes = [
  { path: 'home', component: HomePageComponent },
  { path: 'teams', component: TeamlistPageComponent },
  { path: 'team/:teamName', component: TeamPageComponent },
  
  // Début de la section Lobby Sauvage
  // { path: 'lobby-sauvage', component: LobbySauvageComponent },
  // { path: 'lobby-sauvage/leaderboard', component: LeaderboardComponent },
  // { path: 'lobby-sauvage/current', component: LobbySauvageDetailsComponent },
  // Fin de la section Lobby Sauvage

  { path: 'scrims', component: ScrimsPageComponent },
  { path: 'scrims/history', component: ScrimHistoryComponent },
  { path: 'scrim/:scrimId', component: ScrimDetailsComponent },
  { path: '**', redirectTo: "home" },
];

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' })
  ],
  exports: [],
})
export class AppRoutingModule {
}
