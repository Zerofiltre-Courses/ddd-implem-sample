package tech.zerofiltre.freeland.infra.entrypoints.rest.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI freelandOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Freeland Collaboration Context APIs")
                        .description("APIs for the Collaboration part of the freeland platform")
                        .version("v0.0.1")
                        .license(new License().name("Creative Commons").url("http://creativecommons.org/licenses/by/4.0/"))
                        .contact(
                                new Contact()
                                        .name("Philippe GUEMKAM SIMO")
                                        .email("info@zerofiltre.tech")
                                        .url("https://zerofiltre.tech")
                        )
                );
    }

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfo(
//                "Freeland Collaboration Context APIs",
//                "APIs for the Collaboration part of the freeland platform",
//                "1.0.0",
//                "https://zerofiltre.tech",
//                ,
//                "Creative Commons",
//                "http://creativecommons.org/licenses/by/4.0/",
//                Collections.emptyList()
//        );
//    }
}
