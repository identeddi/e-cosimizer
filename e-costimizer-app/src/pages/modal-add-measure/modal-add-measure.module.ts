import { NgModule } from '@angular/core';
import { IonicModule, IonicPageModule } from 'ionic-angular';
import { ModalAddMeasurePage } from './modal-add-measure';

@NgModule({
  declarations: [
    ModalAddMeasurePage,
  ],
  imports: [
    IonicPageModule.forChild(ModalAddMeasurePage),
  ],
  exports: [
    ModalAddMeasurePage
  ]
})
export class ModalAddMeasurePageModule {}

