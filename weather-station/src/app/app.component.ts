import {Component} from '@angular/core';
import {DataService} from "./services/data.service";
import {WeatherUpdate} from "./model/weather-update";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'weather-station';

  constructor() {
  }

  ngOnInit() {
  }
}
