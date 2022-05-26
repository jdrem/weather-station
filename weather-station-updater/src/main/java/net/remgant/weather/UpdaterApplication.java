package net.remgant.weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.DefaultSaslConfig;
import net.remgant.weather.gson.InstantAdapter;
import net.remgant.weather.model.WeatherUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class UpdaterApplication extends SpringBootServletInitializer {

    final static private Logger log = LoggerFactory.getLogger(UpdaterApplication.class);
    private final Clock clock = Clock.systemUTC();
    private final RabbitTemplate rabbitTemplate;
    static final String directExchangeName = "weather-direct-exchange";
    final private Gson gson;

    public static void main(String[] args) {
        SpringApplication.run(UpdaterApplication.class, args);
    }

    public UpdaterApplication(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        CachingConnectionFactory connectionFactory = (CachingConnectionFactory) rabbitTemplate.getConnectionFactory();
        connectionFactory.getRabbitConnectionFactory().setSaslConfig(DefaultSaslConfig.EXTERNAL);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Instant.class, new InstantAdapter());
        this.gson = builder.create();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody WeatherUpdate data) {
        if (data.getTimestamp() == null)
            data.setTimestamp(Instant.now(clock));
        log.info("Got {}", data);
        rabbitTemplate.convertAndSend(directExchangeName, "weather.updates", gson.toJson(data));
    }

    @RequestMapping("/user")
    public Map<String, Object> user(Principal principal) {
        return Map.of("text", String.format("Hello User %s!", principal.getName()));
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
