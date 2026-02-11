import { Component, ElementRef, Inject, Input, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms'; 
import { TeamService } from 'src/app/service/team.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Team } from 'src/app/models/team.model';
import { NavigationStart, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { UserActions } from "../../../store/user/user.actions";
import { TeamActions } from '../../../store/team/team.actions';
import { TeamFacade } from 'src/app/store/team/team.facade';
import { selectCurrentUserTeam } from '../../../store/team/team.selectors';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-team-form',
  templateUrl: './team-form.component.html',
  styleUrls: ['./team-form.component.scss']
})
export class TeamFormComponent implements OnInit {
  @Input() mode: 'create' | 'edit' = 'create';
  @Input() team?: Team; 
  @Input() decryptedPassword?: string | null;
  @ViewChild('logoInput') logoInputRef!: ElementRef;
  @ViewChild('logoLabel') logoLabelRef!: ElementRef;
  teamForm!: FormGroup; 
  logoError: string | null = null;
  currentUserTeam!: Team | null;
  private routerSubscription: Subscription;
  submitting = false;
 
  constructor(
    private formBuilder: FormBuilder,
    private teamService: TeamService,
    private router: Router,
    public dialogRef: MatDialogRef<TeamFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private store: Store,
    private teamFacade: TeamFacade
  ) { 
    this.routerSubscription = this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.dialogRef.close();
      }
    });
  }

  ngOnInit(): void {
    this.mode = this.data.mode;
    this.team = this.data.team;
    this.decryptedPassword = this.data.decryptedPassword;
    this.store.select(selectCurrentUserTeam)
      .subscribe(team => this.currentUserTeam = team);
    this.initForm();
  }

  getFieldErrors(fieldName: string): ValidationErrors | null {
    const fieldControl = this.teamForm.get(fieldName);
    return fieldControl ? fieldControl.errors : null;
  }
  
  initForm(): void {
    let logoValue = null; 
    if (this.mode === 'edit' && this.team) {
      logoValue = this.team.logo || '';
    }
  
    this.teamForm = this.formBuilder.group({
      name: [this.team?.name || '', [Validators.required, Validators.minLength(3)], this.isValidTeamNameAsync.bind(this)],
      password: [this.decryptedPassword || '', [Validators.required, Validators.minLength(6)]],
      logo: [logoValue, [this.validateOptionalLogoSize()]] 
    });
  
    if (typeof this.team?.logo === 'string') {
      this.loadFileFromUrl(this.team.logo).then(file => {
        this.teamForm.patchValue({ logo: file });
        this.logoLabelRef.nativeElement.textContent = this.team?.logoName;
      });
    }
  }

  async onSubmit() {
    if (this.submitting) return;
  
    this.submitting = true;
  
    await this.isValidTeamNameAsync();
  
    if (this.teamForm.valid) {
      const formData = new FormData();
      formData.append('name', this.teamForm.value.name);
      formData.append('password', this.teamForm.value.password);
      const logoFile = this.teamForm.value.logo;
      if (logoFile && logoFile instanceof File && !this.teamForm.hasError('fileSizeExceeded')) {
        formData.append('logo', logoFile);
      } 
      this.sendFormData(formData);
    } else {
      this.submitting = false; 
      this.showErrors();
    }
  
  }
  
  sendFormData(formData: FormData) {
    const request = this.mode === 'create' ? this.teamService.createTeam(formData) : this.teamService.editTeam(formData);
    request.subscribe({
      next: teamResponse => {
        if (this.mode === 'create') {
          this.store.dispatch(UserActions.setHasOneTeam({ hasOneTeam: true }));
          this.teamFacade.setCurrentUserTeam(teamResponse as Team);
          this.store.dispatch(TeamActions.AddTeam({ team: teamResponse as Team }));
          this.teamFacade.setVisitedTeam(teamResponse as Team);
          this.router.navigate(['/team', this.teamForm.value.name]);
        } else if (this.mode === 'edit') {
          console.log("99999")
          console.log(this.currentUserTeam)
          console.log(this.teamForm.value.name)
          if (this.currentUserTeam) {
            const updatedTeam = { ...this.currentUserTeam, name: this.teamForm.value.name };
            this.teamFacade.setCurrentUserTeam(updatedTeam); 
          }
          this.router.navigate(['/']); 
          setTimeout(() => this.router.navigate(['/team', teamResponse.name])); 
        }
        this.closeDialog();
      },
      error: error => {
        console.log(error);
        // ERROR HANDLING FRONT à faire
      }
    });
  }
  
  showErrors() {
    Object.keys(this.teamForm.controls).forEach(field => {
      const control = this.teamForm.get(field);
      if (control) {
        control.markAsTouched({ onlySelf: true });
        control.updateValueAndValidity(); 
      }
    });
  }
  
  async isValidTeamNameAsync(): Promise<ValidationErrors | null> {
    const teamName = this.teamForm.get('name')?.value;


    if (!teamName) {
      return { required: true, message: 'Team Name is required.' };
  }
  
    if (this.mode === 'edit' && this.currentUserTeam?.name === teamName) {
      console.log('1')
      return null;
    } else {
      try {
        const exists = await this.teamFacade.checkTeamNameExists(teamName).toPromise();
        if (exists) {
          console.log('2')
          return { teamNameExists: true, message: 'Team name already exists.' };
        } else {
          console.log('3')
          return null;
        }
      } catch (error) {
        return { teamNameCheckError: true, message: 'Error checking team name existence.' };
      }
    }
  }
  
  async loadFileFromUrl(url: string): Promise<File> {
    const response = await fetch(url);
    const blob = await response.blob();
    const filename = this.team?.logoName;
    if (filename) {
      return new File([blob], filename);
    } else {
      throw new Error("Le nom de fichier est null ou undefined.");
    }
  }
  
  onLogoSelected(event: any) {
    const file = event.target.files[0]; 
    const maxSize = 1048576; // 1 MB
    if (file) {
      this.teamForm.patchValue({ logo: file });
      if (file.size > maxSize) {
        this.logoError = 'File size exceeded the maximum allowed size.';
        this.teamForm.get('logo')?.setErrors({ 'fileSizeExceeded': true });
        return;
      }
      this.logoError = '';
      this.logoLabelRef.nativeElement.textContent = file.name;
      this.teamForm.get('logo')?.setErrors(null);
    }
  }
  
  closeDialog(): void {
    this.dialogRef.close();
  }
  
  validateOptionalLogoSize(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const file = control.value;
      const maxSize = 1048576; // 1 MB
      if (file && file.size > maxSize) {
        return { 'fileSizeExceeded': true }; 
      }
      return null;
    };
  }
  
  extractDatabaseLogoName(url: string | null ): string {
    const regex = /(?<=api\/images\/).*$/;
    const match = url?.match(regex);
    return match ? match[0] : ""; 
  }

  ngOnDestroy(): void {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

}
