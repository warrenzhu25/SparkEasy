package com.warren;

import com.warren.app.security.CurrentUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warren.backend.data.entity.User;
import com.warren.backend.repositories.UserRepository;
import com.warren.backend.service.UserService;
import com.warren.ui.MainView;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring boot web application initializer.
 */
@SpringBootApplication(scanBasePackageClasses = { MainView.class, Application.class,
		UserService.class }, exclude = ErrorMvcAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = { UserRepository.class })
@EnableJpaAuditing
@EntityScan(basePackageClasses = { User.class })
@EnableScheduling
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	@Bean
	CurrentUser currentUser(){
		return User::new;
	}
}
