import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CreateScrimComponent } from '../create-scrim/create-scrim.component';
import { AuthService } from 'src/app/service/auth/auth.service';
import { User } from 'src/app/models/user.model';
import { Observable } from 'rxjs';
import { SteamAuthService } from 'src/app/service/auth/steam-auth.service';
import { AlertFacade } from 'src/app/store/alert/alert.facade';
import { AlertState } from 'src/app/store/alert/alert.reducer';

@Component({
  selector: 'app-scrims-page',
  templateUrl: './scrims-page.component.html',
  styleUrls: ['./scrims-page.component.scss']
})

export class ScrimsPageComponent implements OnInit {
  isAuthenticated$: boolean = false;
  isAuthenticated: boolean = false;
  user!: User | null;
  alert$: Observable<AlertState>;
  hasOneTeam: boolean = false;

  constructor(
    private dialog: MatDialog,
    private authService: AuthService,
    private steamAuthService: SteamAuthService,
    private alertFacade: AlertFacade
  ) {
    this.alert$ = this.alertFacade.alert$;
  }

  ngOnInit(): void {
    this.authService.isAuthenticated$
      .subscribe(isAuthenticated => {
        this.isAuthenticated$ = isAuthenticated;
        this.isAuthenticated = isAuthenticated;
      });
    this.authService.user$.subscribe(user => {
      this.user = user;
    });
    this.authService.hasOneTeam$.subscribe((hasOneTeam: boolean) => this.hasOneTeam = hasOneTeam);
  }

  tryToCreateScrim(): void {
    if (this.isAuthenticated && this.hasOneTeam) {
      this.dialog.open(CreateScrimComponent);
    } else if (this.isAuthenticated && !this.hasOneTeam) {
      this.alertFacade.setAlert('error', "A Team is required to create a Scrim");
    } else {
      this.steamAuthService.processSteamAuth();
    }
  }

}
