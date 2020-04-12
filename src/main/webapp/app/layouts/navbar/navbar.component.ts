import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/core/language/language.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { LoginService } from 'app/core/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { DataTransfertService } from '../../shared/data-transfert.service';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['navbar.scss']
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  swaggerEnabled?: boolean;
  version: string;
  message!: string;
  produits = 'all';
  carte_arrondi = 'Cartes de visite aux coins arrondis';
  carte_carre = 'Cartes de visite carrées';
  flyers_depliants = 'Flyers et dépliants';
  chemise_documents = 'Chemises porte-documents';
  etiquettes = 'Étiquettes';
  service_graphique = 'Services graphiques';
  sac_papier = 'Sacs en papier';
  mug = 'Mugs';
  mug_magis = 'Magic mugs';
  banderole = 'Banderoles';
  roll_up = 'Roll-ups';
  affiches = 'Affiches';
  invitation_mariage = 'Faire-part de mariage';
  carte_baby_shower = 'Cartes invitation de baby shower';
  carte_invitation_anniversaire = 'Cartes invitation anniversaire';
  menu = 'Menus';
  souvenir_mariage = 'Souvenirs de mariage';
  carton_plume = 'Cartons plumes';
  invitation_fete = 'Invitations de fêtes';

  constructor(
    private loginService: LoginService,
    private languageService: JhiLanguageService,
    private sessionStorage: SessionStorageService,
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private profileService: ProfileService,
    private router: Router,
    private dataTransfert: DataTransfertService
  ) {
    this.version = VERSION ? (VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION) : '';
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.swaggerEnabled = profileInfo.swaggerEnabled;
    });
    this.dataTransfert.currentMessage.subscribe(message => (this.message = message));
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorage.store('locale', languageKey);
    this.languageService.changeLanguage(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  getImageUrl(): string {
    return this.isAuthenticated() ? this.accountService.getImageUrl() : '';
  }

  getMenuClicked(menu: any): any {
    this.dataTransfert.changeMessage(menu);
  }
}
