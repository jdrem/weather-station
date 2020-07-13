import {Component, OnInit} from '@angular/core';
import {DataService} from "../../services/data.service";
import {ChartsModule} from 'ng2-charts';

@Component({
  selector: 'app-chart',
  template: `
    <div>
    <canvas baseChart [chartType]="'line'"
            [datasets]="chartData"
            [labels]="chartLabels"
            [options]="chartOptions">
      [legend]="true"
      height="400'
      width="400"
    </canvas>
    </div>
  `,
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit {
  chartData =[]
  chartLabels = [];
  chartOptions = {
    responsive: true,
    scales: {
      xAxes: [{
        type: 'time',
        time: {
          unit: 'minute'
        }
      }],
      yAxes: [{
        id: 'T',
        type: 'linear',
        position: 'left',
        scaleLabel: {
          labelString: 'Temperature',
          display: true
        },
        ticks: {
          suggestedMin: 20,
          suggestedMax: 80
        }
      }, {
        id: 'H',
        type: 'linear',
        position: 'right',
        scaleLabel: {
          labelString: 'Humidity',
          display: true
        },
        ticks: {
          beginAtZero: true,
          suggestedMax: 100
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
        this.l.push(b.timestamp)
        this.t.push(b.tempF)
        this.h.push(b.humidity)
      })
      this.chartLabels = this.l;
      this.chartData = [
        {data: this.t, label: 'Temerature',  lineTension: 0, fill: false, yAxisID: 'T'},
        {data: this.h, label: 'Humidity', lineTension: 0, fill: false, yAxisID: 'H'}
      ]
    })
  }
}
