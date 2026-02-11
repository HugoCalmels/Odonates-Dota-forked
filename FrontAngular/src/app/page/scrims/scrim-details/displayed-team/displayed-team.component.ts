import { Component, Input } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { MatTableDataSource } from '@angular/material/table';
import { DomSanitizer } from '@angular/platform-browser';
import { ScrimTeam } from 'src/app/models/scrim/scrimTeam.model';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-displayed-team',
  templateUrl: './displayed-team.component.html',
  styleUrls: ['./displayed-team.component.scss']
})
  
export class DisplayedTeamComponent {
  @Input() scrimTeam!: ScrimTeam;
  displayedColumns: string[] = ['avatar', 'userName', 'MMR', 'steamProfile'];
  dataSource = new MatTableDataSource<User>();
  displayedUsers: User[] = [];
  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer
  ) {
    this.matIconRegistry.addSvgIcon(
      'steam-logo',
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/img/white-steam-logo.svg')
    )
  }

  ngOnInit(): void {
    this.sortUsers();
  }
  
  sortUsers(): void{
    if (this.scrimTeam && this.scrimTeam.users) {
      const captain = this.scrimTeam.users.find(user => user.id === this.scrimTeam.squadCaptain.id);
      const otherUsers = this.scrimTeam.users.filter(user => user.id !== this.scrimTeam.squadCaptain.id);
      this.displayedUsers = captain ? [captain, ...otherUsers] : this.scrimTeam.users;
      this.dataSource = new MatTableDataSource<User>(this.displayedUsers);
    }
  }

  ngOnChanges() {
    if (this.scrimTeam && this.scrimTeam.users) {
      this.dataSource.data = this.scrimTeam.users; 
      this.sortUsers();
    }
  }

  navigateToTeamDetail(teamName: string | null) {
    if (teamName) { 
      const teamUrl = `/team/${teamName}`;
      window.open(teamUrl, '_blank');
    } else {
      // Todo : Redirect à gérer "oups this page doesnt exists"
      console.error('Team ID is null or undefined');
    }
  }

  isCaptain(user: User): boolean {
    return this.scrimTeam && this.scrimTeam.squadCaptain && user.id === this.scrimTeam.squadCaptain.id;
  }
}
