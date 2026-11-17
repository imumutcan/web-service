package com.umut.project.database.mywebservice;

import com.umut.project.database.mywebservice.user.User;
import com.umut.project.database.mywebservice.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MyWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyWebServiceApplication.class, args);

    }
    @Bean
    CommandLineRunner useCreator(UserRepository userRepository)
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return string ->{
            for(int i = 0; i<30 ; i++)
            {
                String email = "deneme"+i+"@gmail.com";
                if (!userRepository.existsByEmail(email)) {
                    User user = new User();
                    user.setUsername("deneme-user - " + i);
                    user.setPassword(passwordEncoder.encode("denemedeneme"));
                    user.setEmail(email);
                    user.setActivate(true);
                    user.setRole(com.umut.project.database.mywebservice.user.Role.USER);
                    userRepository.save(user);
                }
            }
        };
    }
}
