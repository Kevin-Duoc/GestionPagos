package com.duoc.Edutech.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API pago de cursos")
                        .version("1.0")
                        .description("API para la compra y venta de cursos")
                        .contact(new Contact()
                                .name("EduTech")
                                .email("contacto@edutech.com")));
    }
}
