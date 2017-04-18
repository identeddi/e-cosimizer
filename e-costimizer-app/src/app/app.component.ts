import { Component, ViewChild } from '@angular/core';
import { Platform, Nav } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { LoginPage } from '../pages/login/login';
import { DashboardPage } from '../pages/dashboard/dashboard';
import { SettingsPage } from '../pages/settings/settings';
import { MeasurePage } from '../pages/measure/measure';
import { User, AuthService } from '../providers/auth-service';
export interface PageInterface {
  title: string;
  component: any;
  icon: string;
  logsOut?: boolean;
  index?: number;
  tabComponent?: any;
}
@Component({
  templateUrl: 'app.html'
})
export class MyApp {

  loggedInUser: User;
  rootPage: any = LoginPage;
  @ViewChild(Nav) nav: Nav;
  dashboardItem: PageInterface = { title: 'Dashboard', component: DashboardPage, icon: 'person' };
  measureItems: PageInterface[] = [
    { title: 'Gas', component: MeasurePage, icon: 'help' },
    { title: 'Strom', component: MeasurePage, icon: 'help' },
    { title: 'Auto', component: MeasurePage, icon: 'help' },
    { title: 'km-Auto', component: MeasurePage, icon: 'help' },
  ];
  settingsItem: PageInterface = { title: 'Einstellungen', component: SettingsPage, icon: 'help' };
  logoutItem: PageInterface = { title: 'Logout', component: LoginPage, icon: 'log-out', logsOut: true };

  constructor(platform: Platform, statusBar: StatusBar, splashScreen: SplashScreen, private auth: AuthService) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      splashScreen.hide();
    });
  }

  openPage(page: PageInterface) {
    // the nav component was found using @ViewChild(Nav)
    // reset the nav to remove previous pages and only have this page
    // we wouldn't want the back button to show in this scenario
    if (page.index) {
      this.nav.setRoot(page.component, { tabIndex: page.index }).catch(() => {
        console.log("Didn't set nav root");
      });
    } else {
      this.nav.setRoot(page.component).catch(() => {
        console.log("Didn't set nav root");
      });
    }


    if (page.logsOut === true) {
      // Give the menu time to close before changing to logged out
      setTimeout(() => {
        this.logout();
      }, 1000);
    }
  }
  isActive(page: PageInterface) {
    let childNav = this.nav.getActiveChildNav();

    // Tabs are a special case because they have their own navigation
    if (childNav) {
      if (childNav.getSelected() && childNav.getSelected().root === page.tabComponent) {
        return 'primary';
      }
      return;
    }

    if (this.nav.getActive() && this.nav.getActive().component === page.component) {
      return 'primary';
    }
    return;
  }


  public logout() {
    this.auth.logout().subscribe(succ => {
      this.nav.setRoot(LoginPage)
    });
  }
}

