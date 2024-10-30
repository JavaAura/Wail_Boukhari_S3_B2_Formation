package com.formation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(createInfo())
            .servers(createServers())
            .externalDocs(createExternalDocs())
            .tags(createTags());
    }
    
    private List<Tag> createTags() {
        return Arrays.asList(
            new Tag().name("Course Management").description("APIs for managing courses"),
            new Tag().name("Student Management").description("APIs for managing students"),
            new Tag().name("Trainer Management").description("APIs for managing trainers"),
            new Tag().name("Classroom Management").description("APIs for managing classrooms")
        );
    }

    private Info createInfo() {
        return new Info()
            .title("Formation Management API")
            .description("API for managing training center resources including courses, students, trainers, and classrooms")
            .version("1.0.0")
            .contact(new Contact()
                .name("Formation Team")
                .email("contact@formation.com")
                .url("https://formation.com"))
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }
    
    private List<Server> createServers() {
        return Arrays.asList(
            new Server()
                .url("http://localhost:8080")
                .description("Development server (H2)"),
            new Server()
                .url("https://api.formation.com")
                .description("Production server (PostgreSQL)")
        );
    }
    
    private ExternalDocumentation createExternalDocs() {
        return new ExternalDocumentation()
            .description("Formation API Documentation")
            .url("https://formation.com/docs");
    }
}
