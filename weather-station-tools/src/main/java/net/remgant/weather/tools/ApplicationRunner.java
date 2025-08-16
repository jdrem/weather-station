package net.remgant.weather.tools;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
@ComponentScan(basePackages = "net.remgant.weather")
@EnableJpaRepositories(basePackages = "net.remgant.weather.dao")
@EntityScan("net.remgant.weather.model")
public class ApplicationRunner implements CommandLineRunner, ApplicationContextAware {
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApplicationRunner.class)
                .web(WebApplicationType.NONE)
                .run(args)
                .close();
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length < 1) {
            throw new IllegalAccessException("no args given");
        }
        CommandLineApplication commandLineApplication = applicationContext.getBean(args[0], CommandLineApplication.class);
        commandLineApplication.run(Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
