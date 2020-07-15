import { Component, OnInit } from '@angular/core';
import {DataService} from "../../services/data.service";
import {WebSocketService} from "../../services/web-socket.service";
import {WeatherUpdate} from "../../model/weather-update";
import {Observable, Subject} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit {

  constructor(private dataService: DataService, private webSocketService: WebSocketService) { }
  data: Observable<Array<WeatherUpdate>>;
  updateList = [];
  updatesChange = new Subject<any>();
  ngOnInit() {
      this.data = this.dataService.data().pipe(
        map(d => {
          this.updateList = d;
          return d;
        }),
      );
      this.webSocketService.webSocket().subscribe((update) => {
        let o: any = JSON.parse(update.body);
        let u: WeatherUpdate = o.weatherUpdate;
        this.updateList.unshift(u);
        this.updatesChange.next(this.updateList);
      })
  }

}
