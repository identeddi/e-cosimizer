import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { HttpModule } from '@angular/http';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';
import { MyApp } from './app.component';
import { LoginPage } from '../pages/login/login';
import { AuthService } from '../providers/auth-service';
import { MeasuresService } from '../providers/measures-service';
import { RegisterPage } from '../pages/register/register';
import { ActivationPage } from '../pages/activation/activation';
import { DashboardPage } from '../pages/dashboard/dashboard';
import { SettingsPage } from '../pages/settings/settings';
import { SettingsGeneralPage } from '../pages/settings-general/settings-general';
import { SettingsOrderListsPage } from '../pages/settings-orderlists/settings-orderlists';
import { MeasurePage } from '../pages/measure/measure';
import { MeasureCurrentPage } from '../pages/measure-current/measure-current';
import { IonicStorageModule } from '@ionic/storage';
@NgModule({
  declarations: [
    MyApp,
    LoginPage,
    RegisterPage,
    ActivationPage,
    DashboardPage,
    SettingsPage,
    SettingsGeneralPage,
    SettingsOrderListsPage,
    MeasurePage,
    MeasureCurrentPage
  ],
  imports: [
    BrowserModule,
    HttpModule,
    IonicModule.forRoot(MyApp),
    IonicStorageModule.forRoot()
  ],

  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    LoginPage,
    RegisterPage,
    ActivationPage,
    DashboardPage,
    SettingsPage,
    SettingsGeneralPage,
    SettingsOrderListsPage,
    MeasurePage,
    MeasureCurrentPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    { provide: ErrorHandler, useClass: IonicErrorHandler },
    AuthService,
    MeasuresService,

  ]
})
export class AppModule { }
