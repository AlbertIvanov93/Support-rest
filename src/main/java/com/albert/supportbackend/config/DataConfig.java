package com.albert.supportbackend.config;

import com.albert.supportbackend.model.Request;
import com.albert.supportbackend.model.RequestStatus;
import com.albert.supportbackend.model.Role;
import com.albert.supportbackend.model.User;
import com.albert.supportbackend.repository.RequestRepository;
import com.albert.supportbackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataConfig {

    @Bean
    public CommandLineRunner userLoader(UserRepository userRepository, RequestRepository requestRepository, PasswordEncoder passwordEncoder) {
        Set<Role> userRole = new HashSet<>();
        userRole.add(Role.USER);
        User user1 = new User("User1", "albert", passwordEncoder.encode("password1"), userRole);
        User user2 = new User("User2", "pavel", passwordEncoder.encode("password2"), userRole);
        User user3 = new User("User3", "ivan", passwordEncoder.encode("password3"), userRole);
        User user4 = new User("User4", "evgeniy", passwordEncoder.encode("password4"), userRole);
        User user5 = new User("User5", "dmitriy", passwordEncoder.encode("password5"), userRole);
        User user6 = new User("User6", "valentin", passwordEncoder.encode("password6"), userRole);

        Set<Role> operatorRole = new HashSet<>();
        operatorRole.add(Role.OPERATOR);

        User operator1 = new User("Operator1", "konstantin", passwordEncoder.encode("operator1"), operatorRole);

        Set<Role> adminRole = new HashSet<>();
        adminRole.add(Role.ADMIN);

        User admin1 = new User("Admin1", "inokentiy", passwordEncoder.encode("admin1"), adminRole);


        Request request1 = new Request(user1, "Something1");
        Request request2 = new Request(user1, "Something2");
        Request request3 = new Request(user1, "Something3");
        Request request4 = new Request(user1, "Something4");
        Request request5 = new Request(user1, "Something5");
        Request request6 = new Request(user1, "Something6");
        Request request7 = new Request(user1, "Something7");
        Request request8 = new Request(user2, "Something8");
        Request request9 = new Request(user2, "Something9");
        Request request10 = new Request(user2, "Something10");
        Request request11 = new Request(user3, "Something11");
        Request request12 = new Request(user4, "Something12");
        Request request13 = new Request(user5, "Something13");
        request13.setStatus(RequestStatus.SENT);
        request4.setStatus(RequestStatus.SENT);
        request5.setStatus(RequestStatus.SENT);
        request6.setStatus(RequestStatus.SENT);
        request7.setStatus(RequestStatus.SENT);
        request8.setStatus(RequestStatus.SENT);
        request9.setStatus(RequestStatus.SENT);
        request11.setStatus(RequestStatus.SENT);

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                userRepository.save(user1);
                userRepository.save(user2);
                userRepository.save(user3);
                userRepository.save(user4);
                userRepository.save(user5);
                userRepository.save(user6);
                userRepository.save(operator1);
                userRepository.save(admin1);

                requestRepository.save(request1);
                requestRepository.save(request2);
                requestRepository.save(request3);
                requestRepository.save(request4);
                requestRepository.save(request5);
                requestRepository.save(request6);
                requestRepository.save(request7);
                requestRepository.save(request8);
                requestRepository.save(request9);
                requestRepository.save(request10);
                requestRepository.save(request11);
                requestRepository.save(request12);
                requestRepository.save(request13);
            }
        };
    }
}
