import { Component, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatCheckbox, MatCheckboxChange } from '@angular/material/checkbox';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { Scrim } from 'src/app/models/scrim/scrim.model';
import { User } from 'src/app/models/user.model';
import { ScrimFacade } from 'src/app/store/scrim/scrim.facade';
import { selectUser } from '../../../../store/user/user.selectors';
import { Subscription } from 'rxjs';
import { NavigationStart, Router } from '@angular/router';
import * as moment from 'moment-timezone';

@Component({
  selector: 'app-player-selection-modal',
  templateUrl: './player-selection-modal.component.html',
  styleUrls: ['./player-selection-modal.component.scss']
})
export class PlayerSelectionModalComponent implements OnInit {
  playerSelectionForm!: FormGroup;
  selectedPlayersControl = new FormControl<User[]>([], Validators.required);
  scrim: Scrim | null = null;
  filteredUsers!: User[];
  currentUser!: User | null;
  @ViewChildren(MatCheckbox) checkboxes!: QueryList<MatCheckbox>;
  private routerSubscription: Subscription;
  userTimeZone: string;

  constructor(
    public dialogRef: MatDialogRef<PlayerSelectionModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private formBuilder: FormBuilder,
    private scrimFacade: ScrimFacade,
    private store: Store,
    private router: Router
  ) {
    this.userTimeZone = moment.tz.guess();
    this.routerSubscription = this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.dialogRef.close();
      }
    });
  }

  ngOnInit(): void {
    this.subscribeToStore();
    this.initializeForm();
    this.subscribeToScrim();
    this.setupFormListeners();
    setTimeout(() => this.validatePlayerSelection(), 0); 
  }

  private subscribeToStore(): void {
    this.store.select(selectUser).subscribe(user => {
      this.currentUser = user;
      this.filterUsers();
    });
  }

  private filterUsers(): void {
    if (this.currentUser && this.currentUser.team && this.currentUser.team.users) {
      this.filteredUsers = this.currentUser.team.users.filter(user => user.id !== this.currentUser!.id);
    }
  }

  private initializeForm(): void {
    this.playerSelectionForm = this.formBuilder.group({
      selectedPlayers: this.selectedPlayersControl
    });
  }

  private subscribeToScrim(): void {
    this.data.scrim.subscribe((scrim: Scrim) => {
      this.scrim = scrim;
      this.filterUsers();
      this.validatePlayerSelection();
    });
  }

  private setupFormListeners(): void {
    this.playerSelectionForm.get('selectedPlayers')?.valueChanges.subscribe(() => {
      this.validatePlayerSelection();
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  confirmSelection(): void {
    if (this.playerSelectionForm.valid) {
      this.sendScrimProposal();
      this.dialogRef.close(this.selectedPlayersControl.value);
    } else {
      this.playerSelectionForm.markAllAsTouched();
    }
  }

  onPlayerSelectionChange(event: MatCheckboxChange, player: User): void {
    let selectedPlayers = this.selectedPlayersControl.value || [];
    const maxPlayerCount = this.getMaxPlayerCount();

    if (event.checked) {
      if (!selectedPlayers.some(p => p.id === player.id)) {
        if (selectedPlayers.length < maxPlayerCount) {
          selectedPlayers.push(player);
        } else {
          event.source.checked = false;
        }
      }
    } else {
      selectedPlayers = selectedPlayers.filter(p => p.id !== player.id);
    }

    this.selectedPlayersControl.setValue(selectedPlayers);
    this.validatePlayerSelection();
  }

  validatePlayerSelection(): boolean {
    const selectedPlayers = this.selectedPlayersControl.value || [];
    const maxPlayerCount = this.getMaxPlayerCount();

    if (selectedPlayers.length !== maxPlayerCount) {
      this.selectedPlayersControl.setErrors({ 'incorrectNumber': true });
      return false;
    } else {
      this.selectedPlayersControl.setErrors(null);
      return true;
    }
  }

  getMaxPlayerCount(): number {
    return (this.scrim?.firstScrimTeam?.users?.length ?? 1) - 1;
  }

  getMissingPlayersCountMessage(): string {
    const selectedPlayers = this.selectedPlayersControl.value || [];
    const maxPlayerCount = this.getMaxPlayerCount();
    const missingPlayersCount = maxPlayerCount - selectedPlayers.length;
    return `${missingPlayersCount}`;
  }

  sendScrimProposal(): void {
    const selectedPlayerIds = this.selectedPlayersControl?.value?.map((player: User) => player.id) || [];
    if (this.scrim && this.scrim.id && this.currentUser) {
      this.scrimFacade.sendScrimProposal(this.scrim.id, [...selectedPlayerIds, this.currentUser.id], this.userTimeZone);
    }
  }

  isSelectPlayersVisible(): boolean {
    return true;
  }

  resetPlayerSelection(): void {
    this.selectedPlayersControl.setValue([]);
  }

  resetCheckboxes(): void {
    this.checkboxes.forEach((checkbox) => {
      checkbox.checked = false;
    });
  }

  hasMultipleUsersInFirstTeam(): boolean | undefined {
    return this.scrim?.firstScrimTeam?.users && this.scrim.firstScrimTeam.users.length > 1;
  }
}