package ua.epan.elearn.selection.committee.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class SelectionCommitteeSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SelectionCommitteeSpringApplication.class, args);
    }

}
