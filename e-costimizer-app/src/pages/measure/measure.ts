import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { MeasureCurrentPage } from '../measure-current/measure-current';
import { MeasureHistoryPage } from '../measure-history/measure-history';
import { PowerMeasureType } from '../../providers/measures-service';

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
  tab2Root: any = MeasureHistoryPage;
  title : string  = "";
  public powerMeasureType : PowerMeasureType
  
  constructor(public navCtrl: NavController, public navParams: NavParams) {
  }

  ionViewDidLoad() {    
    this.powerMeasureType = this.navParams.get("powerMeasureType");
    this.title = this.powerMeasureType.typeName;
    console.log('MeasurePage ionViewDidLoad Measure with type ' + this.powerMeasureType.typeName);
  }


}
