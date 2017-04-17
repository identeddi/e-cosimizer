import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
import { AuthService } from '../../providers/auth-service';

@Component({
  selector: 'page-register',
  templateUrl: 'register.html',
})
export class RegisterPage {
  createSuccess = false;
  registerCredentials = { userName: '', firstName: '', lastName: '', email: '', password: '', passwordConfirmation: '' };

  constructor(private nav: NavController, private auth: AuthService, private alertCtrl: AlertController) { }

  public register() {
    this.auth.register(this.registerCredentials).subscribe(success => {
      if (success) {
        this.createSuccess = true;
        this.showPopup("OK", "Konto angelegt");
      } else {
        this.showPopup("Fehler", "Konto konnte nicht angelegt werden");
      }
    },
      error => {
        this.showPopup("Fehler", error);
      });
  }

  showPopup(title, text) {
    let alert = this.alertCtrl.create({
      title: title,
      subTitle: text,
      buttons: [
        {
          text: 'OK',
          handler: data => {
            if (this.createSuccess) {
              this.nav.popToRoot();
            }
          }
        }
      ]
    });
    alert.present();
  }

}
