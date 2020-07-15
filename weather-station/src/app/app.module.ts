import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HttpClientModule} from "@angular/common/http";
import { ChartModule } from 'angular2-chartjs';
import { ChartComponent } from './weather-station/chart/chart.component';
import { NavbarComponent } from './weather-station/navbar/navbar.component';
import { CurrentComponent } from './weather-station/current/current.component';
import { TableComponent } from './weather-station/table/table.component';
import {ChartsModule} from "ng2-charts";
import {InjectableRxStompConfig, RxStompService, rxStompServiceFactory} from "@stomp/ng2-stompjs";
import {rxStompConfig} from "./rx-stomp.config";

@NgModule({
  declarations: [
    AppComponent,
    ChartComponent,
    NavbarComponent,
    CurrentComponent,
    TableComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ChartModule,
    ChartsModule
  ],
  providers: [
    {
      provide: InjectableRxStompConfig,
      useValue: rxStompConfig
    },
    {
      provide: RxStompService,
      useFactory: rxStompServiceFactory,
      deps: [InjectableRxStompConfig]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
