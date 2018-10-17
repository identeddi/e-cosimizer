import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/mergeMap';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';
import { AuthService,User } from '../providers/auth-service';

export class PowerMeasureHistoryDTO {
  id: number;
  measureDate : Date;
  nextMeasureDate : Date;
  measureValue : number;
  dailyConsumption : number;
  dataType: string;
  powerMeasureType : PowerMeasureType;
}

export class PowerMeasureType {
  id : number;
  typeEnum : MeasureTypeEnum;
  typeName : string;
  referenceId : string;
  enabled : boolean;
  entryNotification : PeriodicNotification;

}
export enum MeasureTypeEnum {
  GAS = 1, STROM = 2, AUTO = 3, DSL = 4, MOBILE = 5, OTHER = 99
}
export enum PeriodicNotification {
	NEVER, WEEKLY, MONTHLY, YEARLY

}

export class PowerMeasure{
  measureDate : Date;
  measureValue : number;
  powerMeasureType : PowerMeasureType;
}

@Injectable()
export class MeasuresService {
  currentUser: User;
  response: any;
  errorMessage: string;
  constructor(public http: Http, public authService: AuthService) {

  }
  public getLastMeasures() : Observable<PowerMeasureHistoryDTO[]> {
    return this.authService.getToken().mergeMap(authcToken => {
      let headers: Headers = new Headers();

      headers.append("Authorization", 'Token ' + authcToken);
      headers.append("Accept", 'application/json');
      headers.append("Content-Type", 'application/json');
      
      let options = new RequestOptions({ headers: headers });
      var url = 'https://localhost:8443/rest/power/measure/last';
      return this.http.get(url, options).map(resp => resp.json())
    });

  }
  public getCurrentMeasure(powerMeasureType: PowerMeasureType) : Observable<PowerMeasureHistoryDTO> {
    return this.authService.getToken().mergeMap(authcToken => {
      let headers: Headers = new Headers();

      headers.append("Authorization", 'Token ' + authcToken);
      headers.append("Accept", 'application/json');
      headers.append("Content-Type", 'application/json');
       let options = new RequestOptions({ headers: headers });
      var url = 'https://localhost:8443/rest/power/type/'+ powerMeasureType.id+'/measure';
      return this.http.get(url, options).map(resp => resp.json())
    });

  }
  public getPowerMeasureTypes() : Observable<PowerMeasureType[]> {
    return this.authService.getToken().mergeMap(authcToken => {
      let headers: Headers = new Headers();

      headers.append("Authorization", 'Token ' + authcToken);
      headers.append("Accept", 'application/json');
      headers.append("Content-Type", 'application/json');
       let options = new RequestOptions({ headers: headers });
      var url = 'https://localhost:8443/rest/power/powermeasuretypes';
      return this.http.get(url, options).map(resp => resp.json())
    });

  }

  public addNewMeasure(powerMeasure : PowerMeasure) : Observable<any> {
    // this.authService.getToken().subscribe( authcToken => {
    //   let headers: Headers = new Headers();

    //   headers.append("Authorization", 'Token '  + authcToken);
    //   headers.append("Accept", 'application/json');
    //   headers.append("Content-Type", 'application/json');
    //    let options = new RequestOptions({ headers: headers });
    //   var url = 'https://localhost:8443/rest/power/type/'+ powerMeasure.powerMeasureType.id + '/measure';
    //   return this.http.post(url,powerMeasure, options).subscribe(resp => {
    //     console.log(resp);
    //   });

    // });
    return this.authService.getToken().mergeMap(authcToken => {
      let headers: Headers = new Headers();

      headers.append("Authorization", 'Token '  + authcToken);
      headers.append("Accept", 'application/json');
      headers.append("Content-Type", 'application/json');
       let options = new RequestOptions({ headers: headers });
      var url = 'https://localhost:8443/rest/power/type/'+ powerMeasure.powerMeasureType.id + '/measure';
      return this.http.post(url,powerMeasure, options);
    });
  }


}
