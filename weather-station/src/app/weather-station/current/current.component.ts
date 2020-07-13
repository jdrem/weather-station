import { Component, OnInit } from '@angular/core';
import {DataService} from "../../services/data.service";

@Component({
  selector: 'app-current',
  template: `
    <h3>Current Conditions</h3>
    <table class="table table-striped">
      <tbody>
      <tr>
        <td style="text-align:center">Temp</td>
        <td style="text-align:center">{{t}}</td>
      </tr>
      <tr>
        <td style="text-align:center">Humidity</td>
        <td style="text-align:center">{{h}}</td>
      </tr>
      <tr>
        <td style="text-align:center">Pressure</td>
        <td style="text-align:center">{{p}}</td>
      </tr>
      </tbody>
    </table>
  `,
  styleUrls: ['./current.component.css']
})
export class CurrentComponent implements OnInit {

  constructor(private dataService: DataService) { }

  t;
  h;
  p;

  ngOnInit() {
    this.dataService.data().subscribe(a => {
      this.t = a[0].tempF;
      this.h = a[0].tempC;
      this.p = a[0].pressure;
    })
  }

}
