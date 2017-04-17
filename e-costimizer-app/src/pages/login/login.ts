import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { AlertController, LoadingController, Loading } from 'ionic-angular';
import { AuthService } from '../../providers/auth-service';
import { RegisterPage } from '../register/register';
import { HomePage } from '../home/home';
import { ActivationPage } from '../activation/activation';

@Component({
  selector: 'page-login',
  templateUrl: 'login.html',
})
export class LoginPage {
  loading: Loading;
  registerCredentials = { email: '', password: '' };

  constructor(private nav: NavController, private auth: AuthService, private alertCtrl: AlertController, private loadingCtrl: LoadingController) {
    let activationCode: string = this.getActivationCodeFromURL();
    if (activationCode != null) {
      nav.push(ActivationPage, {
        activationcode: activationCode
      });

    }
  }
  getActivationCodeFromURL() {
    let re = /[?&]([^=#&]+)=([^&#]*)/g;
    let match: RegExpExecArray;
    let isMatch = true;
    let matches = [];
    let activationcode: string = null;
    while (isMatch) {
      match = re.exec(window.location.href);
      if (match !== null && match.length >= 2) {
        if (decodeURIComponent(match[1]) === "activationcode") {
          activationcode = match[2];

        }
      }
      else {
        isMatch = false;
      }
    }

    if (activationcode != null) {
      console.log("activationcode found: " + activationcode);
    }
    else {
      console.log("activationcode not found");

    }
    return activationcode;
  }
  public createAccount() {
    this.nav.push(RegisterPage);
  }
  public login() {
    this.showLoading()
    this.auth.login(this.registerCredentials).subscribe(allowed => {
      if (allowed) {
        setTimeout(() => {
          this.loading.dismiss();
          this.nav.setRoot(HomePage)
        });
      } else {
        this.showError("Zugrif verweigert");
      }
    },
      error => {
        this.showError(error);
      });
  }

  showLoading() {
    this.loading = this.loadingCtrl.create({
      content: 'Bitte warten...'
    });
    this.loading.present();
  }

  showError(text) {
    setTimeout(() => {
      this.loading.dismiss();
    });

    let alert = this.alertCtrl.create({
      title: 'Fehler',
      subTitle: text,
      buttons: ['OK']
    });
    alert.present(prompt);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad Login');
  }

}
