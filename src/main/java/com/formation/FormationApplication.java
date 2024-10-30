package com.formation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FormationApplication {

	private static final Logger logger = LoggerFactory.getLogger(FormationApplication.class);
	private final Environment environment;

	public FormationApplication(Environment environment) {
		this.environment = environment;
	}

	public static void main(String[] args) {
		SpringApplication.run(FormationApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void displayApplicationUrls() {
		String serverPort = environment.getProperty("server.port", "8080");
		String activeProfile = String.join(", ", environment.getActiveProfiles());
		
		StringBuilder urlInfo = new StringBuilder("\n--------------------------------------------\n");
		urlInfo.append("Application URLs:\n");
		urlInfo.append("--------------------------------------------\n");
		urlInfo.append("Active Profile: ").append(activeProfile).append("\n");
		urlInfo.append("Swagger UI: http://localhost:").append(serverPort).append("/swagger-ui.html\n");		
		if (activeProfile.contains("dev")) {
			urlInfo.append("H2 Console: http://localhost:").append(serverPort).append("/h2-console\n");
		}
		
		urlInfo.append("SonarQube: http://localhost:9000\n");
		urlInfo.append("--------------------------------------------\n");
		
		logger.info(urlInfo.toString());
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*");
			}
		};
	}

}
