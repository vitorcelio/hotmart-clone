package com.hotmart.account;

import com.hotmart.account.models.Role;
import com.hotmart.account.models.User;
import com.hotmart.account.repositories.RoleRepository;
import com.hotmart.account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

import static com.hotmart.account.enums.Roles.ADMIN;

@EnableKafka
@SpringBootApplication
public class AccountApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
//        Role role = roleRepository.findById(ADMIN.getId()).get();
//
//        User user = User.builder()
//                .name("Vítor")
//                .email("admin")
//                .roles(Set.of(role))
//                .password(new BCryptPasswordEncoder().encode("123456"))
//                .build();
//
//        userRepository.save(user);
    }

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }

}
