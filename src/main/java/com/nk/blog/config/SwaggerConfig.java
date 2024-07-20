package com.nk.blog.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
    /**
     * + * A description of the publicApi function.
     * + *
     * + * @return description of the return value
     * +
     */
    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder().group("blog-service").pathsToMatch("/**").build();
    }

    /**
     * + * A description of the custompenAPI function.
     * + *
     * + * @return description of the return value
     * +
     */
    @Bean
    OpenAPI custompenAPI() {
        Server server = new Server();
        server.setUrl("/");
        return new OpenAPI()
                .servers(List.of(server))
                // .components(new Components().addSecuritySchemes("bearer-key", new
                // SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info().title("Blog Service:: API Documentation")
                        .version("v1"))
                .externalDocs(new ExternalDocumentation()
                        .description("Blog-Service GitHub Repository")
                        .url("https://github.com/Nikhil12894/Blog-Service"));
    }

}
