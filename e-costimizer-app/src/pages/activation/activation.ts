import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { AuthService } from '../../providers/auth-service';
import { AlertController, LoadingController, Loading } from 'ionic-angular';
/**
 * Generated class for the Activation page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@Component({
  selector: 'page-activation',
  templateUrl: 'activation.html',
})
export class ActivationPage {
  loading: Loading;
  activationcode: string;
  userResponse: string;

  constructor(public navCtrl: NavController, public navParams: NavParams, private auth: AuthService, private alertCtrl: AlertController, private loadingCtrl: LoadingController) {
    this.activationcode = this.navParams.get('activationcode');
    this.showLoading();
    this.auth.enableAccount(this.activationcode).subscribe(message => {
      this.loading.dismiss();
      console.log(message);
      this.showPopup("Aktivierung erfolgreich", message);
      window.location.href = window.location.pathname;
    },
      error => {
        this.showError(error);
        window.location.href = window.location.pathname;
        this.navCtrl.popToRoot();
      });
    this.loading.present();
  }

  showError(text) {
    setTimeout(() => {
      this.loading.dismiss();
    });

    let alert = this.alertCtrl.create({
      title: 'Fail',
      subTitle: text,
      buttons: ['OK']
    });
    alert.present(prompt);
  }
  showPopup(title, text) {
    let alert = this.alertCtrl.create({
      title: title,
      subTitle: text,
      buttons: [
        {
          text: 'OK',
          handler: data => {
            this.navCtrl.popToRoot();
          }
        }
      ]
    });
    alert.present();
  }
  showLoading() {
    this.loading = this.loadingCtrl.create({
      content: 'Please wait...'
    });
    this.loading.present();
  }
}
