package net.remgant.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "net.remgant.weather.dao")
@EntityScan("net.remgant.weather.model")
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public static void main(String[] args) {
        initInitialContext();
        SpringApplication.run(Application.class, args);
    }

    private static void initInitialContext() {
        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        String dbUrl = System.getProperty("db.url");
        String dbUser = System.getProperty("db.user");
        String dbPwd = System.getProperty("db.pwd");
        DataSource dataSource = new DriverManagerDataSource(dbUrl, dbUser, dbPwd);
        builder.bind("java:/comp/env/jdbc/weather", dataSource);
        try {
            builder.activate();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public DataSource dataSource() {
        Context initContext;
        try {
            initContext = new InitialContext();
            return (DataSource) initContext.lookup("java:/comp/env/jdbc/weather");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
