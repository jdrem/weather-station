import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {WeatherUpdate} from "../model/weather-update";
import {share} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient) { }

  data(): Observable<WeatherUpdate[]> {
    return this.http.get<WeatherUpdate[]>('/api/data').pipe(share());
  }
}
