import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Subscription} from "rxjs";
import {NavbarService} from "../../service/navbar.service";
import {AuthService} from 'src/app/service/auth/auth.service';
import {User} from 'src/app/models/user.model';
import {NgbDropdown} from "@ng-bootstrap/ng-bootstrap";


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit, OnDestroy {
  private toggleButton: any;
  private sidebarVisible: boolean;

  private subscriptions: Subscription[];

  @ViewChild('nav') nav: any;
  @ViewChild('drop') drop: any;
  @ViewChild('lobbyDropdown') lobbyDropdown!: NgbDropdown;
  @ViewChild('scrimsDropdown') scrimsDropdown!: NgbDropdown;

  backgroundColor: any;
  textColor: any;
  private classe: string = 'nav-down';

  constructor(private element: ElementRef,
              private navBarService: NavbarService, private authService: AuthService) {
    this.sidebarVisible = false;
    this.subscriptions = [];
  }

  ngOnInit() {
    const navbar: HTMLElement = this.element.nativeElement;
    this.toggleButton = navbar.getElementsByClassName('navbar-toggler')[0];
    this.subscriptions.push(
      this.navBarService.toggleEvent.subscribe(
        showNavbar => {
          if (showNavbar) {
            this.navbarVisible();
          } else {
            this.navbarHidden();
          }
        }
      )
    );
    this.initStyle();
  }

  openDrop(_: any) {
    this.drop.open();
  }

  closeDrop(_: any) {
    this.drop.close();
  }

  sidebarOpen() {
    const toggleButton = this.toggleButton;
    const html = document.getElementsByTagName('html')[0];
    setTimeout(function () {
      toggleButton.classList.add('toggled');
    }, 500);
    html.classList.add('nav-open');

    this.sidebarVisible = true;
  };

  sidebarClose() {
    const html = document.getElementsByTagName('html')[0];
    this.toggleButton.classList.remove('toggled');
    this.sidebarVisible = false;
    html.classList.remove('nav-open');
  };

  sidebarToggle() {
    if (!this.sidebarVisible) {
      this.sidebarOpen();
    } else {
      this.sidebarClose();
    }
  };

  private navbarVisible() {
    if (this.classe !== 'nav-down') {
      this.classe = 'nav-down';
    }
  }

  private navbarHidden() {
    if (this.classe !== 'nav-up') {
      this.classe = 'nav-up';
    }
  }

  initStyle() {
    this.backgroundColor = {
      'background-color': '#292824',
    };
    this.textColor = {
      'color': '#c7c7c5',
      'opacity': .9
    };
  }

  getNavbarWrapperClasses() {
    return this.classe;
  }

  ngOnDestroy(): void {
    this.subscriptions.map(it => it.unsubscribe());
  }

  logAllUsers() {
    // bouton pour tester le flow du jwt, genre apres expiration du jwt
    this.authService.getAllUsers().subscribe({
      next: (users: User[]) => {
        console.log('Liste des utilisateurs :', users);
      },
      error: (error: any) => {
        console.error('Erreur lors de la récupération des utilisateurs :', error);
      }
    });
  }

  toggleDropdown(toggle: boolean, dropdown: string) {
    if (dropdown === 'lobbyDropdown') {
      toggle ? this.lobbyDropdown.open() : this.lobbyDropdown.close();
    } else if (dropdown === 'scrimsDropdown') {
      toggle ? this.scrimsDropdown.open() : this.scrimsDropdown.close();
    }
  }
  
}
