package com.sda.webproject.configuration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.sda.webproject")
@EntityScan("com.sda.webproject.entities")
@EnableJpaRepositories("com.sda.webproject.repositories")
public class SpringConfig {

}
