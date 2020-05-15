package com.copyleft.snippos.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Getter
public class AuthProperties {

    private final Authentication authentication = new Authentication();

    @Getter
    public static class Authentication {

        private final Jwt jwt = new Jwt();

        @Getter
        @Setter
        public static class Jwt {

            private String secret;

            private long tokenValidityInSeconds = 1800;

            private long tokenValidityInSecondsForRememberMe = 2592000;

        }
    }
}
