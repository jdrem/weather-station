import {Component, OnInit} from '@angular/core';
import {DataService} from "../../services/data.service";
import {WeatherUpdate} from "../../model/weather-update";
import {WebSocketService} from "../../services/web-socket.service";

@Component({
  selector: 'app-current',
  template: `
    <h3 style="text-align:center">Current Conditions</h3>
    <table class="table table-striped">
      <tbody>
      <tr>
        <td style="text-align:center">Time</td>
        <td style="text-align:center">{{ts | date:'short'}}</td>
      </tr>
      <tr>
        <td style="text-align:center">Temp (F)</td>
        <td style="text-align:center">{{tf + 0.499 | number:'1.0-0'}}&deg;</td>
      </tr>
      <tr>
        <td style="text-align:center">Temp (C)</td>
        <td style="text-align:center">{{tc + 0.499 | number:'1.0-0'}}&deg;</td>
      </tr>
      <tr>
        <td style="text-align:center">Humidity</td>
        <td style="text-align:center">{{h + 0.499 | number:'1.0-0'}}%</td>
      </tr>
      <tr>
        <td style="text-align:center">Pressure</td>
        <td style="text-align:center">{{p | number: '1.1-1'}} mbar</td>
      </tr>
      </tbody>
    </table>
  `,
  styleUrls: ['./current.component.css']
})
export class CurrentComponent implements OnInit {

  constructor(private dataService: DataService, private webSocketService: WebSocketService) {
  }

  ts;
  tf;
  tc;
  h;
  p;

  ngOnInit() {
    this.dataService.data().subscribe(a => {
      this.ts = a[0].timestamp;
      this.tf = a[0].tempF;
      this.tc = a[0].tempC;
      this.h = a[0].humidity;
      this.p = a[0].pressure;
    })

    this.webSocketService.webSocket().subscribe((update) => {
      console.log('got message from ws: ' + update.body);
      let o: any = JSON.parse(update.body);
      let u: WeatherUpdate = o.weatherUpdate;
      this.ts = u.timestamp;
      this.tf = u.tempF;
      this.tc = u.tempC;
      this.h = u.humidity;
      this.p = u.pressure;
    });
  }

}
