import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

/**
 * Generated class for the MeasureHistoryPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@Component({
  selector: 'page-measure-history',
  templateUrl: 'measure-history.html',
})
export class MeasureHistoryPage {
  powerMeasureTypeid = this.navParams.get('powerMeasureTypeid');
 
  constructor(public navCtrl: NavController, public navParams: NavParams) {
  }

  ionViewDidEnter() {
    this.powerMeasureTypeid = this.navParams.get("powerMeasureTypeid");
    console.log('MeasureHistoryPage ionViewDidLoad Measure with type ' + this.powerMeasureTypeid);
   }

}
