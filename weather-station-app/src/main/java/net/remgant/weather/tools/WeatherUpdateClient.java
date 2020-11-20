package net.remgant.weather.tools;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.remgant.weather.model.WeatherUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Scanner;

public class WeatherUpdateClient {
    private static final String URL = "ws://localhost:9099/update";

    public static void main(String[] args) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(WeatherUpdate.class, new WeatherUpdateDeserializer());
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        ObjectMapper mapper = converter.getObjectMapper();
        mapper.registerModule(module);

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(converter);
        StompSessionHandler sessionHandler = new StompSessionHandler();
        stompClient.connect(URL, sessionHandler);

        new Scanner(System.in).nextLine(); // Don't close immediately.
    }

    static class WeatherUpdateDeserializer extends StdDeserializer<WeatherUpdate> {

        public WeatherUpdateDeserializer() {
            super((Class<?>) null);
        }

        @Override
        public WeatherUpdate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            node = node.findPath("weatherUpdate");
            Instant timestamp = Instant.parse(node.get("timestamp").asText("1970-01-01T00:00:00.000Z"));
            double tempF = node.get("tempF").asDouble(0.0);
            double tempC = node.get("tempC").asDouble(0.0);
            double humidity = node.get("humidity").asDouble(0.0);
            double pressure = node.get("pressure").asDouble(0.0);
            return new WeatherUpdate(timestamp, tempF, tempC, humidity, pressure);
        }
    }

    static class StompSessionHandler extends StompSessionHandlerAdapter {
        private final static Logger log = LoggerFactory.getLogger(StompSessionHandler.class);

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            log.info("New session established : " + session.getSessionId());
            session.subscribe("/topic/weatherEventUpdate", this);
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            log.error("Got an exception", exception);
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return WeatherUpdate.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            WeatherUpdate msg = (WeatherUpdate) payload;
            log.info("received: {}", msg);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            log.warn("problem in transport layer {}", exception.getMessage(), exception);
        }
    }
}
