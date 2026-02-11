import { Component, OnInit, Input } from '@angular/core';
import { Store } from '@ngrx/store';
import { AuthService } from 'src/app/service/auth/auth.service';
import { SteamAuthService } from 'src/app/service/auth/steam-auth.service';
import { UserActions } from "../../../store/user/user.actions";
import { DomSanitizer } from '@angular/platform-browser';
import { MatIconRegistry } from '@angular/material/icon';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  @Input() textColor: any;
  isAuthenticated: boolean = false;
  user: User | null = null;

  constructor(private authService: AuthService, private steamAuthService: SteamAuthService, private store: Store, private domSanitizer: DomSanitizer, private matIconRegistry: MatIconRegistry) {
    this.steamAuthService = steamAuthService;
    this.matIconRegistry.addSvgIcon(
      'steam-logo',
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/img/steam-logo-navbar-white.svg')
    )
  }

  ngOnInit(): void {
    this.authService.isAuthenticated$.subscribe((isAuthenticated: boolean) => {
      this.isAuthenticated = isAuthenticated;  
      if (isAuthenticated) {
        this.authService.user$
        .pipe()
        .subscribe(user => {
          this.user = user;
        });
      }
    });
  }

  logoutUser() {
    this.authService.processLogout();
    this.store.dispatch(UserActions.setHasOneTeam({ hasOneTeam: false }));
  }

  loginUser(): void {
    this.steamAuthService.processSteamAuth();

  }

}
