package net.remgant.weather.tools;

import net.remgant.weather.dao.AuthorityRepository;
import net.remgant.weather.dao.UserRepository;
import net.remgant.weather.model.Authority;
import net.remgant.weather.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component("AddRESTUser")
public class AddRESTUser implements CommandLineApplication {

    final private UserRepository userRepository;
    final private AuthorityRepository authorityRepository;

    public AddRESTUser(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    public void run(String... args){
        String username = args[0];
        String password = args[1];
        boolean enabled = true;
        String firstName = args[2];
        String lastName = args[3];

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(password);
        User user = new User(username, encryptedPassword, enabled, lastName, firstName);
        userRepository.save(user);
        Authority authority = new Authority(username, "ROLE_USER");
        authorityRepository.save(authority);
    }
}
