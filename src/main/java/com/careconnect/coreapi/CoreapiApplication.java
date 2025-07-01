package com.careconnect.coreapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulith;

@SpringBootApplication
@Modulith(
    systemName = "CareConnect Core API",
    sharedModules = "common"
)
public class CoreapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreapiApplication.class, args);
	}

}
