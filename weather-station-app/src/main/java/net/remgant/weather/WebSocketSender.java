package net.remgant.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketSender implements ApplicationListener<WeatherUpdateEvent> {
    final static private Logger log = LoggerFactory.getLogger(WebSocketSender.class);
    private SimpMessagingTemplate template;

    public WebSocketSender(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void onApplicationEvent(WeatherUpdateEvent weatherUpdateEvent) {
         log.info("Got event for {}",weatherUpdateEvent.getWeatherUpdate());
         template.convertAndSend("/topic/weatherEventUpdate", weatherUpdateEvent);
    }
}
