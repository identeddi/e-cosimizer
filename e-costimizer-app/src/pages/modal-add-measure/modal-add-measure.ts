import { Subject } from 'rxjs/Subject';
import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ViewController } from 'ionic-angular';
import { PowerMeasureHistoryDTO, MeasuresService, PowerMeasure, PowerMeasureType } from '../../providers/measures-service';

/**
 * Generated class for the ModalAddMeasurePage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-modal-add-measure',
  templateUrl: 'modal-add-measure.html',
})
export class ModalAddMeasurePage {

  bookingDate : string = new Date().toISOString();
  bookingMeasure : number = 4711;
  powerMeasureType : PowerMeasureType;
  currentMeasure : PowerMeasureHistoryDTO;

  constructor(public navCtrl: NavController,public viewCtrl : ViewController, public navParams: NavParams, public measureService: MeasuresService) {
    this.currentMeasure = navParams.get('currentMeasure');
    this.powerMeasureType = navParams.get('powerMeasureType');
    if(this.currentMeasure != null){
      this.bookingMeasure = this.currentMeasure.measureValue;
    }
    else{
      this.bookingMeasure = 0;

    }
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ModalAddMeasurePage');
    
  }
  public closeModal(){
    this.viewCtrl.dismiss();
  }

  public applyModal(){
    let powerMeasure : PowerMeasure = new PowerMeasure();
    powerMeasure.measureDate = new Date(this.bookingDate);
    powerMeasure.measureValue = this.bookingMeasure;
    powerMeasure.powerMeasureType = this.powerMeasureType;
    this.measureService.addNewMeasure(powerMeasure).subscribe(data =>{
      this.viewCtrl.dismiss();
    });

  }
  

}
