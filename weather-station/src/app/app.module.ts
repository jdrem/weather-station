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
    ChartModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
