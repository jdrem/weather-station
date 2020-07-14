import { Injectable } from '@angular/core';
import {webSocket, WebSocketSubject} from "rxjs/webSocket";
import {WeatherUpdate} from "../model/weather-update";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  public websocket: WebSocketSubject<WeatherUpdate>;
  constructor() {
    this.websocket = webSocket('ws://localhost:9099/update')
    console.log('web socket created')
  }
}
