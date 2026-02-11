import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { GameMode } from '../../../models/scrim/enums/gameMode.enum';
import { Scrim } from 'src/app/models/scrim/scrim.model';
import { MatDialogRef } from '@angular/material/dialog';
import { ScrimFacade } from 'src/app/store/scrim/scrim.facade';
import * as moment from 'moment-timezone';
import { Store } from '@ngrx/store';
import { selectUser } from '../../../store/user/user.selectors';
import { Team } from 'src/app/models/team.model';
import { User } from 'src/app/models/user.model';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { MatCheckbox, MatCheckboxChange } from '@angular/material/checkbox';
import { Subscription } from 'rxjs';
import { NavigationStart, Router } from '@angular/router';
import { selectCurrentUserTeam } from 'src/app/store/team/team.selectors';

@Component({
  selector: 'create-scrim-form',
  templateUrl: './create-scrim.component.html',
  styleUrls: ['./create-scrim.component.scss'],
  animations: [
    trigger('fadeInOut', [
      state('void', style({
        opacity: 0
      })),
      transition('void <=> *', [
        animate(150)
      ]),
    ])
  ]
})
  
export class CreateScrimComponent implements OnInit {
  scrimForm!: FormGroup;
  gameModes: string[] = Object.values(GameMode);
  userTimeZone: string;
  minDate: Date;
  currentUserTeam!: Team | null;
  currentUser!: User | null;
  selectedPlayersControl = new FormControl<User[]>([], Validators.required);
  filteredUsers!: User[];
  @ViewChildren(MatCheckbox) checkboxes!: QueryList<MatCheckbox>;
  private routerSubscription: Subscription;
  isSubmitting = false; 

  constructor(
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<CreateScrimComponent>,
    private scrimFacade: ScrimFacade,
    private store: Store,
    private router: Router
  ) {
    this.userTimeZone = moment.tz.guess();
    this.minDate = new Date();
    this.routerSubscription = this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.dialogRef.close();
      }
    });
  }

  ngOnInit(): void {
    this.subscribeToStore();
    this.initializeForm();
    this.setupFormListeners();
  }

  private subscribeToStore(): void {
    this.store.select(selectUser).subscribe(user => {
      this.currentUser = user;
    });
    this.store.select(selectCurrentUserTeam).subscribe(team => {
      this.currentUserTeam = team;
    });
    this.filterUsers();
  }
  
  private filterUsers(): void {
    if (this.currentUserTeam !== null && this.currentUserTeam.users !== null) {
      this.filteredUsers = this.currentUserTeam.users.filter(user => user.id !== this.currentUser!.id);
    }
  }

  private initializeForm(): void {
    this.scrimForm = this.formBuilder.group({
      gameMode: ['', Validators.required],
      startDate: ['', Validators.required],
      startTime: ['', Validators.required],
      selectedPlayers: this.selectedPlayersControl
    });
  }

  private setupFormListeners(): void {
    this.scrimForm.get('gameMode')?.valueChanges.subscribe(() => {
      this.updateSelectedPlayersVisibility();
      this.resetPlayerSelection();
      this.validatePlayerSelection();
    });
    this.validatePlayerSelection();
  }

  onSubmit(): void {
    if (this.isSubmitting) return;

    if (this.scrimForm.valid && this.validateStartDate() && this.validatePlayerSelection()) {
      this.isSubmitting = true;
      const newScrim = this.createScrimObject();
      this.createScrim(newScrim);
      this.dialogRef.close();
      this.isSubmitting = false;
    } else {
      this.scrimForm.markAllAsTouched();
    }
  }

  isControlInvalid(controlName: string): boolean {
    const control = this.scrimForm.get(controlName);
    return control ? control.invalid && (control.dirty || control.touched) : false;
  }

  resetPlayerSelection(): void {
      this.selectedPlayersControl.setValue([]);
  }

  private validateStartDate(): boolean {
    const { startDate, startTime } = this.scrimForm.value;
    const startDateTime = this.combineDateAndTime(startDate, startTime);
    const currentTime = new Date();

    if (startDateTime.getTime() < currentTime.getTime()) {
      this.scrimForm.controls['startTime'].setErrors({ 'pastDate': true });
      return false;
    } else {
      this.scrimForm.controls['startTime'].setErrors(null);
      return true;
    }
  }

  private combineDateAndTime(date: string, time: string): Date {
    const dateTime = new Date(date);
    const [hours, minutes] = time.split(':').map(part => parseInt(part, 10));
    dateTime.setHours(hours, minutes);
    dateTime.setMinutes(dateTime.getMinutes() - dateTime.getTimezoneOffset());
    return dateTime;
  }

  private createScrimObject(): Scrim {
    const { gameMode, startDate, startTime } = this.scrimForm.value;
    const startDateTime = this.combineDateAndTime(startDate, startTime);
    return { gameMode, startDateTime };
  }

  private createScrim(newScrim: Scrim): void {
    const selectedPlayerIds = (this.selectedPlayersControl.value ?? [])
      .map(player => player.id);
    if (this.currentUser) {
      this.scrimFacade.createScrim(newScrim, this.userTimeZone, [...selectedPlayerIds, this.currentUser.id]);
    }
  }

  dateFilter = (date: Date | null): boolean => {
    const currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0);
    return !date || date >= currentDate;
  }

  isSelectPlayersVisible(): boolean {
    const selectedGameMode: string = this.scrimForm.value.gameMode;
    return selectedGameMode !== '' && selectedGameMode !== GameMode.MID_ONLY_1V1;
  }

  updateSelectedPlayersVisibility(): void {
    if (this.isSelectPlayersVisible()) {
      this.selectedPlayersControl.enable();
    } else {
      this.selectedPlayersControl.disable();
      this.selectedPlayersControl.setValue([]);
    }
    this.resetPlayerSelectionErrors();
    this.resetCheckboxes();
  }

  resetPlayerSelectionErrors(): void {
    this.selectedPlayersControl.setErrors(null);
  }

  resetCheckboxes(): void {
    this.checkboxes.filter(checkbox => checkbox.id !== 'specialCheckbox').forEach((checkbox) => {
      checkbox.checked = false;
    });
  }

  validatePlayerSelection(): boolean {
    const selectedGameMode: string = this.scrimForm.value.gameMode;
    const selectedPlayers: User[] = this.selectedPlayersControl.value || [];
    const requiredPlayerCount = this.getRequiredPlayerCount(selectedGameMode);

    if (selectedPlayers.length !== requiredPlayerCount) {
      this.selectedPlayersControl.setErrors({ 'incorrectNumber': true });
      return false;
    } else {
      this.selectedPlayersControl.setErrors(null);
      return true;
    }
  }

  private getRequiredPlayerCount(gameMode: string): number {
    switch (gameMode) {
      case GameMode.RANDOM_DRAFT_5V5:
      case GameMode.ALL_PICK_5V5:
      case GameMode.CAPTAINS_MODE_5V5:
        return 4;
      case GameMode.MID_ONLY_2V2:
        return 1;
      default:
        return 0;
    }
  }

  onPlayerSelectionChange(event: MatCheckboxChange, player: User): void {
    let selectedPlayers = this.selectedPlayersControl.value || [];
    const maxPlayerCount = this.getRequiredPlayerCount(this.scrimForm.value.gameMode);

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

  getMissingPlayersCountMessage(): string {
    const selectedGameMode: string = this.scrimForm.value.gameMode;
    const selectedPlayers: User[] = this.selectedPlayersControl.value || [];
    const requiredPlayerCount = this.getRequiredPlayerCount(selectedGameMode);
    return `${requiredPlayerCount - selectedPlayers.length}`;
  }

}