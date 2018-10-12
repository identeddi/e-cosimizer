import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { MeasuresService } from '../../providers/measures-service';
/**
 * Generated class for the Dashboard page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@Component({
  selector: 'page-dashboard',
  templateUrl: 'dashboard.html',
})
export class DashboardPage {
  lastMeasures: any;

  constructor(public navCtrl: NavController, public navParams: NavParams, measureService: MeasuresService) {
    measureService.getLastMeasures().subscribe(measures =>
      this.lastMeasures = measures);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad Dashboard');
  }
}
