import {isDevMode, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {NavbarComponent} from "./common/navbar/navbar.component";
import {RouterLink, RouterOutlet} from "@angular/router";
import {NgbDropdown, NgbDropdownItem, NgbDropdownMenu, NgbDropdownToggle} from "@ng-bootstrap/ng-bootstrap";
import {HomePageComponent} from "./page/home/home-page/home-page.component";
import {AppRoutingModule} from "./app.routing";
import {FooterComponent} from "./common/footer/footer.component";
import {StoreModule} from '@ngrx/store';

import {authReducer} from './store/auth/auth.reducer';
import {userReducer} from "./store/user/user.reducer";
import {teamReducer} from "./store/team/team.reducer"

import { scrimReducer } from "./store/scrim/scrim.reducer"
import { alertReducer } from "./store/alert/alert.reducer"

import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {environment} from 'src/environment/environment';
import {LoginComponent} from './common/navbar/login/login.component';

import {AuthInterceptor} from './helpers/auth.interceptor';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {TeamModule} from './page/teams/team.module';
import {MatSelectModule} from '@angular/material/select';
import {MatSelectCountryModule} from '@angular-material-extensions/select-country';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {EffectsModule} from '@ngrx/effects';
import {TeamEffects} from './store/team/team.effects';
import {LobbySauvageModule} from "./page/lobby-sauvages/lobby-sauvage.module";
import * as fromLobbySauvage from './store/lobby-sauvage/lobby-sauvage.reducer';
import { LobbySauvageEffects } from './store/lobby-sauvage/lobby-sauvage.effects';

import { ScrimsModule } from './page/scrims/scrims.module';
import { MatSortModule } from '@angular/material/sort';
import { ScrimEffects } from './store/scrim/scrim.effects';
import { SharedModule } from './shared/shared.module';
import { AlertEffects } from './store/alert/alert.effects';
import { MatIconModule } from '@angular/material/icon';
import { CguModalComponent } from './common/footer/cgu-modal/cgu-modal.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomePageComponent,
    LoginComponent,
    FooterComponent,
    CguModalComponent
  ],
  imports: [
    BrowserModule,
    RouterLink,
    MatIconModule,
    TeamModule,
    LobbySauvageModule,
    ScrimsModule,
    SharedModule,
    HttpClientModule,
    MatSelectCountryModule.forRoot('de'),
    MatSelectModule,
    NgbDropdown,
    NgbDropdownToggle,
    NgbDropdownMenu,
    NgbDropdownItem,
    FormsModule,
    CommonModule,
    MatSortModule,
    RouterOutlet,
    AppRoutingModule,
    BrowserAnimationsModule,
    StoreModule.forRoot({ 
      auth: authReducer, 
      user: userReducer, 
      team: teamReducer,
      scrim: scrimReducer,
      alert: alertReducer
    }, { initialState: {}}), 
    !environment.production ? StoreDevtoolsModule.instrument({
      maxAge: 25,
      logOnly: environment.production,
    }) : [],
    EffectsModule.forRoot([TeamEffects, ScrimEffects, AlertEffects]),
    StoreModule.forFeature(fromLobbySauvage.lobbySauvagesFeatureKey, fromLobbySauvage.reducer),
    EffectsModule.forFeature([LobbySauvageEffects]),
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  exports: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
