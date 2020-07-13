import { Component } from '@angular/core';
import {DataService} from "./services/data.service";
import {WeatherUpdate} from "./model/weather-update";
import {Observable} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'weather-station';
  data: Observable<WeatherUpdate[]>;

  tdata: any = <any> {
    labels: ['11:00', '11:05', '11:10', '11:15', '11:20', '11:25'],
    datasets: [{
      yAxisID: 'A',
      fill: false,
      lineTension: 0,
      label: 'Temperature',
      data: [28, 29, 32, 33, 30, 27],
      borderWidth: 3,
      borderColor: 'rgba(255, 99, 132, 1)',
    },
      {
        yAxisID: 'B',
        fill: false,
        lineTension: 0,
        label: 'Humidity',
        data: [54, 52, 50, 49, 55, 60],
        borderWidth: 3,
        borderColor: 'rgba(54, 162, 235, 1)',
      }]
  }

  options = {
    scales: {
      yAxes: [{
        id: 'A',
        type: 'linear',
        position: 'left',
        ticks: {
          beginAtZero: true
        }
      },{
        id: 'B',
        type: 'linear',
        position: 'right',
        ticks: {
          beginAtZero: true
        }
      }]
    }
  }

  constructor(private dataService: DataService) {
  }
  ngOnInit() {
    this.data = this.dataService.data();
  }
}
