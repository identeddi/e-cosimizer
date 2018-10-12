import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/mergeMap';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';
import { AuthService } from '../providers/auth-service';
export class User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  zipcode: string;
  username: string;


  constructor(public authService: AuthService) {

  }
}

@Injectable()
export class MeasuresService {
  currentUser: User;
  response: any;
  errorMessage: string;
  constructor(public http: Http, public authService: AuthService) {

  }
  public getLastMeasures() {
    return this.authService.getToken().mergeMap(authcToken => {
      let headers: Headers = new Headers();

      headers.append("Authorization", 'Token ' + authcToken);
      headers.append("Content-Type", 'application/json');
      let options = new RequestOptions({ headers: headers });
      var url = 'https://localhost:8443/rest/power/measure/last';
      return this.http.get(url, options).map(resp => resp.json())
    });

  }


}
