import { NgModule } from "@angular/core";
import { PageHeroComponent } from "../common/page-hero/page-hero.component";
import { CommonModule } from "@angular/common";
import { CustomAlertComponent } from "../common/custom-alert/custom-alert.component";
import { MatIconModule } from "@angular/material/icon";
import { MmrEstimatePipe } from "../utils/mmr-estimate.pipe";

@NgModule({

  declarations: [
    PageHeroComponent,
    CustomAlertComponent,
    MmrEstimatePipe
  ],
  imports: [
    CommonModule,
    MatIconModule,
  ],
  exports: [
    PageHeroComponent,
    CustomAlertComponent,
    MmrEstimatePipe
  ]
 })
 export class SharedModule { }