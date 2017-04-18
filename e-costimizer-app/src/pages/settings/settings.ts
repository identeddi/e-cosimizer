import { Component } from '@angular/core';
import { NavParams } from 'ionic-angular';

import { SettingsGeneralPage } from '../settings-general/settings-general';
import { SettingsOrderListsPage } from '../settings-orderlists/settings-orderlists';
/* Generated class for the Settings page.
*
* See http://ionicframework.com/docs/components/#navigation for more info
* on Ionic pages and navigation.
*/
@Component({
  selector: 'page-settings',
  templateUrl: 'settings.html',
})
export class SettingsPage {
  // set the root pages for each tab
  tab1Root: any = SettingsGeneralPage;
  tab2Root: any = SettingsOrderListsPage;
  mySelectedIndex: number;

  constructor(navParams: NavParams) {
    this.mySelectedIndex = navParams.data.tabIndex || 0;
  }


  ionViewDidLoad() {
    console.log('ionViewDidLoad Settings');
  }

}
