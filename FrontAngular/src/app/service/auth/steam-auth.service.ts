import { Injectable } from '@angular/core';
import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root',
})
export class SteamAuthService {

  private backendUrl = environment.backendUrl

  processSteamAuth(): void {
    const loginParams = this.generateLoginParams();
    const steamLoginUrl = this.buildSteamLoginUrl(loginParams);

    window.location.href = steamLoginUrl;
  }

  generateLoginParams(): Map<string, string> {
    const currentRoute = window.location.pathname;
    const loginParams = new Map<string, string>();
    loginParams.set('openid.ns', 'http://specs.openid.net/auth/2.0');
    loginParams.set('openid.mode', 'checkid_setup');
    loginParams.set('openid.return_to', `${this.backendUrl}/steamAuth/process-steam-login?currentRoute=${currentRoute}`); 
    loginParams.set('openid.realm', `${this.backendUrl}`); // 4646 server backend à fix
    loginParams.set('openid.identity', 'http://specs.openid.net/auth/2.0/identifier_select');
    loginParams.set('openid.claimed_id', 'http://specs.openid.net/auth/2.0/identifier_select');

    return loginParams;
  }

  private buildSteamLoginUrl(loginParams: Map<string, string>): string {
    const baseUrl = 'https://steamcommunity.com/openid/login';
    const queryString = Array.from(loginParams.entries())
      .map(([key, value]) => `${key}=${value}`)
      .join('&');
    return `${baseUrl}?${queryString}`;
  }

}
