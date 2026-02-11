import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatTableModule} from '@angular/material/table';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatDialogModule} from "@angular/material/dialog";
import {MatIconModule} from '@angular/material/icon';
import {LeaderboardComponent} from "./leaderboard/leaderboard.component";
import { LobbySauvageDetailsComponent } from './lobby-sauvage-details/lobby-sauvage-details.component';
import {LobbySauvageComponent} from "./lobby-list/lobby-sauvage.component";
import {RouterLink} from "@angular/router";
import {AppModule} from "../../app.module";
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [
    LobbySauvageComponent,
    LeaderboardComponent,
    LobbySauvageDetailsComponent
    ],
  imports: [
    CommonModule,
    FormsModule,
    SharedModule,
    MatPaginatorModule,
    MatTableModule,
    MatDialogModule,
    BrowserAnimationsModule,
    MatIconModule,
    ReactiveFormsModule,
    NgOptimizedImage,
    RouterLink
  ]
})
export class LobbySauvageModule { }
