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
  data: Observable<WeatherUpdate[]>;
  l: string[];
  t: number[];
  h: number[];
  tdata: any = <any> {
    labels: [],
    datasets: [{
      yAxisID: 'A',
      fill: false,
      lineTension: 0,
      label: 'Temperature',
      data: [],
      borderWidth: 3,
      borderColor: 'rgba(255, 99, 132, 1)',
    },
      {
        yAxisID: 'B',
        fill: false,
        lineTension: 0,
        label: 'Humidity',
        data: [],
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
      }, {
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
    this.data.subscribe(a => {
      this.l = Array.of<string>();
      this.t = Array.of<number>();
      this.h = Array.of<number>();
      a.forEach(b => {
        this.l.push(b.timestamp.substr(11, 5))
        this.t.push(b.tempF)
        this.h.push(b.humidity)
      })
      this.tdata.labels = this.l;
      this.tdata.datasets[0].data = this.t;
      this.tdata.datasets[1].data = this.h;
    })
  }
}
