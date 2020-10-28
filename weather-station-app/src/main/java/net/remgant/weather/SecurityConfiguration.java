package net.remgant.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final static Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    private final static String REALM = "REMGANT_REALM";

    @Value("${rest.user:}")
    private String restUser;
    @Value("${rest.password:}")
    private String restPassword;
    @Value("${rest.admin.user:}")
    private String adminUser;
    @Value("${rest.admin.password:}")
    private String adminPassword;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> userDetailsManagerConfigurer = auth.inMemoryAuthentication();
        userDetailsManagerConfigurer.withUser(restUser).password("{noop}" + restPassword).roles("USER");
        if (!adminUser.equals(""))
            userDetailsManagerConfigurer.withUser(adminUser).password("{noop}" + adminPassword).roles("USER", "ADMIN");
    }
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/update").hasRole("USER")
                .antMatchers("/api/deleteAll").hasRole("ADMIN")
                .and()
                .httpBasic()
                .authenticationEntryPoint(basciAuthEntryPoint());
    }

    @Bean
    public BasicAuthenticationEntryPoint basciAuthEntryPoint() {
        return new BasicAuthenticationEntryPoint() {
            @Override
            public void afterPropertiesSet() {
                setRealmName(REALM);
                super.afterPropertiesSet();
            }

            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.addHeader("WWW-Authenticate", "Basic realm=" + REALM);
                log.warn("Authentication failure: {}", authException.getMessage());
            }
        };
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
