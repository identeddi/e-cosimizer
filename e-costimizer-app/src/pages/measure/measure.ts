import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { MeasureCurrentPage } from '../measure-current/measure-current';

/**
 * Generated class for the Measure page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */

@Component({
  selector: 'page-measure',
  templateUrl: 'measure.html',
})
export class MeasurePage {
  tab1Root: any = MeasureCurrentPage;

  constructor(public navCtrl: NavController, public navParams: NavParams) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad Measure');
  }

}
