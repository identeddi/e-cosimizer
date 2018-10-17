import { PowerMeasure } from './../../providers/measures-service';
import { Component } from '@angular/core';
import { NavController, NavParams, ModalController } from 'ionic-angular';
import { MeasuresService, PowerMeasureHistoryDTO, PowerMeasureType } from '../../providers/measures-service';

/**
 * Generated class for the MeasureCurrent page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@Component({
  selector: 'page-measure-current',
  templateUrl: 'measure-current.html',
})
export class MeasureCurrentPage {
  powerMeasureType : PowerMeasureType = this.navParams.get('powerMeasureType');
  currentMeasure : PowerMeasureHistoryDTO;

  
  constructor(public navCtrl: NavController, public navParams: NavParams, public measureService : MeasuresService, public modalCtrl : ModalController) {
  }

  ionViewDidEnter() {
    this.powerMeasureType = this.navParams.get("powerMeasureType");
    this.measureService.getCurrentMeasure(this.powerMeasureType).subscribe(measures =>
      this.currentMeasure = measures);
    console.log('MeasureCurrentPage ionViewDidLoad Measure with type ' + this.powerMeasureType.typeName);
   }

   addMeasureDialog(){
    console.log('addMeasureDialog ' + this.powerMeasureType.id);
    var data = { currentMeasure : this.currentMeasure,
      powerMeasureType : this.powerMeasureType };
    var modalPage = this.modalCtrl.create('ModalAddMeasurePage',data);
    
    modalPage.onDidDismiss(powerMeasure => {
      this.ionViewDidEnter();
    });
    modalPage.present();
   }

}
