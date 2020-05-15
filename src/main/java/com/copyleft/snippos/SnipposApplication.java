package com.copyleft.snippos;

import com.copyleft.snippos.config.AuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AuthProperties.class})
public class SnipposApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnipposApplication.class, args);
	}

}
