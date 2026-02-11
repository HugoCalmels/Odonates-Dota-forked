import { NgModule } from "@angular/core";
import { ScrimsPageComponent } from "./scrims-page/scrims-page.component";
import { CreateScrimComponent } from "./create-scrim/create-scrim.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelectModule } from "@angular/material/select";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { MatNativeDateModule } from "@angular/material/core";
import { MatDatepickerModule } from '@angular/material/datepicker';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { CommonModule } from "@angular/common";
import { ScrimListComponent } from './scrim-list/scrim-list.component';
import { MatTableModule } from "@angular/material/table";
import { MatSortModule } from "@angular/material/sort";
import { BrowserModule } from "@angular/platform-browser";
import { MatDialogModule } from "@angular/material/dialog";
import { ScrimDetailsComponent } from './scrim-details/scrim-details.component';
import { SharedModule } from "src/app/shared/shared.module";
import { DisplayedTeamComponent } from './scrim-details/displayed-team/displayed-team.component';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from "@angular/material/icon";
import { DateTimeFormatPipe } from "src/app/utils/date-time-format.pipe";
import { GameModeFormatPipe } from "src/app/utils/game-mode-format.pipe";
import { HttpClientModule } from "@angular/common/http";
import { ScrimHistoryComponent } from './scrim-history/scrim-history.component';
import { MmrEstimatePipe } from "src/app/utils/mmr-estimate.pipe";
import { MmrAveragePipe } from "src/app/utils/mmr-average.pipe";
import { PlayerSelectionModalComponent } from './scrim-details/player-selection-modal/player-selection-modal.component';
import { ProposalDetailsComponent } from './scrim-details/proposal-details/proposal-details.component';
import { DeleteScrimModalComponent } from './scrim-details/delete-scrim-modal/delete-scrim-modal.component';

@NgModule({
  declarations: [
    ScrimsPageComponent,
    CreateScrimComponent,
    ScrimListComponent,
    ScrimDetailsComponent,
    DisplayedTeamComponent,
    PlayerSelectionModalComponent,
    DateTimeFormatPipe,
    GameModeFormatPipe,
    MmrAveragePipe,
    ScrimHistoryComponent,
    PlayerSelectionModalComponent,
    ProposalDetailsComponent,
    DeleteScrimModalComponent
    ],
  imports: [
    CommonModule,
    BrowserModule,
    MatIconModule,
    SharedModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatSelectModule,
    HttpClientModule,
    MatInputModule,
    MatButtonModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatTableModule,
    MatSortModule,
    MatDialogModule,
    MatCheckboxModule,
    FormsModule,
  ],
  providers: [MmrAveragePipe]
})
export class ScrimsModule { }