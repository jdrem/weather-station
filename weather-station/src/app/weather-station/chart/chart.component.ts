import {Component, OnInit} from '@angular/core';
import {DataService} from "../../services/data.service";

@Component({
  selector: 'app-chart',
  template: `
    <chart type="line" [data]="data" [options]="options"></chart>
  `,
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit {
  data: any = <any>{
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
  l: string[];
  t: number[];
  h: number[];
  ngOnInit() {
   this.dataService.data().subscribe(a => {
      this.l = Array.of<string>();
      this.t = Array.of<number>();
      this.h = Array.of<number>();
      a.forEach(b => {
        this.l.push(b.timestamp.substr(11, 5))
        this.t.push(b.tempF)
        this.h.push(b.humidity)
      })
      this.data.labels = this.l;
      this.data.datasets[0].data = this.t;
      this.data.datasets[1].data = this.h;
    })
  }

}
