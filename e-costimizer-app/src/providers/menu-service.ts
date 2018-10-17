import { PageInterface } from './../app/app.component';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Http, Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/Rx';
import { AuthService,User } from './auth-service';
import { MeasurePage } from '../pages/measure/measure';
import { Observable } from 'rxjs/Rx';
import { PowerMeasureType, MeasuresService } from './measures-service';

export class MenuEntry{
  caption : string;
  url: string;
  powerMeasureType : PowerMeasureType ;
}

@Injectable()
export class MenuService {
  menuSubject: Subject<string> = new Subject<string>();
  menuItems = [];

  constructor(public http: Http, public authService: AuthService, public measuresService : MeasuresService) {

  }
  public updateMenu(userid){
    var menu = this.measuresService.getPowerMeasureTypes().subscribe(data => {
      this.menuItems = [];
      data.forEach((powerMeasureType: PowerMeasureType) =>{
        this.menuItems.push({
          id: powerMeasureType.id,
          title: powerMeasureType.typeName,
          component: MeasurePage,
          powerMeasureType : powerMeasureType
        });

        this.menuSubject.next("new menu items have arrived");
      });
      
    })
    
  }


}
