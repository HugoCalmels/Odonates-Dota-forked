import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamPageComponent } from './team-page/team-page.component';
import { TeamlistPageComponent } from './teamlist-page/teamlist-page.component';
import { FormsModule } from '@angular/forms';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from "@angular/material/dialog";
import { ReactiveFormsModule } from '@angular/forms'
import {MatIconModule} from '@angular/material/icon';
import { TeamParametersComponent } from './team-page/team-parameters/team-parameters.component';
import { TeamFormComponent } from './team-form/team-form.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { DeleteTeamModalComponent } from './team-page/delete-team-modal/delete-team-modal.component';
import { JoinTeamModalComponent } from './team-page/join-team-modal/join-team-modal.component';
import { FormatTeamDatePipe } from 'src/app/utils/format-team-date.pipe';
import { MmrEstimatePipe } from 'src/app/utils/mmr-estimate.pipe';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@NgModule({
  declarations: [
    TeamPageComponent,
    TeamlistPageComponent,
    TeamParametersComponent,
    TeamFormComponent,
    DeleteTeamModalComponent,
    JoinTeamModalComponent,
    FormatTeamDatePipe,
    ],
  imports: [
    CommonModule,
    FormsModule,
    SharedModule,
    MatPaginatorModule,
    MatTableModule,
    MatDialogModule,
    BrowserAnimationsModule,
    MatProgressSpinnerModule,
    MatIconModule,
    ReactiveFormsModule
  ]
})
export class TeamModule { }
