import { Injectable } from '@angular/core';
import {RxStompService} from "@stomp/ng2-stompjs";
import {share} from "rxjs/operators";
import {IMessage} from "@stomp/stompjs";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  ws: Observable<IMessage>;

  constructor(private rxStompService: RxStompService) {
      this.ws = rxStompService.watch('/topic/weatherEventUpdate').pipe(share());
  }

  webSocket() {
    return this.ws;
  }
}
