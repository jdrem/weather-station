import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {WeatherUpdate} from "../model/weather-update";
import {share} from "rxjs/operators";
import {of} from 'rxjs';
import {shareReplay} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient) {
  }

  private d: WeatherUpdate[];
  private o: Observable<any>;

  data(): Observable<WeatherUpdate[]> {
   /* return this.http.get<WeatherUpdate[]>('/api/data').pipe(shareReplay(
      {
        bufferSize: 3,
        refCount: true
      }
    ));*/

    if (this.d) {
      console.log("got result, sending d")
      return of(this.d);
    } else if (this.o) {
      console.log("result pending, sending o")
      return this.o;
    } else {
      // Get the last days worth of data, but no more than 288 items (i.e. every 5 min over 24 hours)
      let t = Math.floor((new Date()).getTime() / 1000) - (24*60*60);
      this.o = this.http.get<WeatherUpdate[]>('/api/data?size=288&start='+t).pipe(
        share()
      );
      this.o.subscribe(r => {
        console.log("processing subscription")
        this.o = null;
        this.d = r;
      })
      console.log("called http, sending o")
      return this.o;
    }
  }
}
