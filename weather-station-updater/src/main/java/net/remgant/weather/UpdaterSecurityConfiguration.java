package net.remgant.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UpdaterSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${user.names: }")
    private String userNames;

    @Value("${admin.names: }")
    private String adminNames;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated().and()
                .x509()
                .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                .userDetailsService(userDetailsService());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        for (String userName : userNames.split(",")) {
            inMemoryUserDetailsManager.createUser(new User(userName, "",
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER")));
        }
        for (String userName : adminNames.split(",")) {
            inMemoryUserDetailsManager.createUser(new User(userName, "",
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN")));
        }
        return inMemoryUserDetailsManager;
    }
}
