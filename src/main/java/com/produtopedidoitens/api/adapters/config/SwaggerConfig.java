package com.produtopedidoitens.api.adapters.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApiInformation() {
        Server localServer = new Server().url("http://localhost:8080").description("Local server");
        Contact contact = new Contact().email("vanderleik@yahoo.com.br").name("Vanderlei Kleinschmidt").url("https://www.linkedin.com/in/vanderlei-kleinschmidt-a1557731/");
        Info info = new Info().contact(contact).title("Produtos e Pedidos API")
                .description("API para cadastro de produtos e pedidos")
                .version("v0.0.1")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));

        return new OpenAPI()
                .info(info)
                .addServersItem(localServer)
                .externalDocs(new ExternalDocumentation().description("Springdoc OpenAPI 3.0").url("https://springdoc.org/"));
    }
}
