import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { User,AuthService } from '../../providers/auth-service';
import { LoginPage } from '../login/login';
@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  loggedInUser : User;
  constructor(private nav: NavController, private auth: AuthService ){
  this.loggedInUser = this.auth.getCurrentUser();
  
 }
 

  public logout() {
    this.auth.logout().subscribe(succ => {
        this.nav.setRoot(LoginPage)
    });
  }

}
