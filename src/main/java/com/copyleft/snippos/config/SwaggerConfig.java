package com.copyleft.snippos.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

/**
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String API_INFO_TITLE = "Snippos";

    public static final String API_INFO_DESCRIPTION = "Copy-Left : Snippos";

    public static final String API_INFO_VERSION = "API V1.0";

    public static final String API_INFO_TERMS_AND_CONDITION = "Terms of service";


    public static final String API_INFO_CONTACT_NAME = "copy-left";

    public static final String API_INFO_CONTACT_ADDRESS = "copy-left.netlify.app";

    public static final String API_INFO_EMAIL = "copy-left.netlify.app";

    public static final String API_INFO_LICENSE_URL = "copy-left.netlify.app";

    public static final String API_INFO_LICENSE = "License of API";

    public static final String SEQUENCE_PATTERN = "%010d";

    public static final String TOKEN_PREFIX = "Bearer";

    public static final String AUTH_HEADER_PARAM = "Authorization";



    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.copyleft.snippos.rest"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()))
                .apiInfo(apiInfo());
    }

    /**
     * API Metadata
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
                API_INFO_TITLE,
                API_INFO_DESCRIPTION,
                API_INFO_VERSION,
                API_INFO_TERMS_AND_CONDITION,
                new Contact(API_INFO_CONTACT_NAME, API_INFO_CONTACT_ADDRESS, API_INFO_EMAIL),
                API_INFO_LICENSE, API_INFO_LICENSE_URL, Collections.emptyList());
    }

    /**
     * Build security context for Swagger Codegen
     * @return
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    /**
     * Swagger Scheme of token Authentication, default permission as global
     * @return
     */
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference("token", authorizationScopes));
    }

    /**
     * let swagger know how to provide authentication token
     * @return
     */
    public SecurityScheme apiKey() {
        return new ApiKey("token", HttpHeaders.AUTHORIZATION, "header");
    }
}