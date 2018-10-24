package com.rspace.rspaceimgmetadata.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


//@EnableEurekaClient
@EnableJpaRepositories(basePackages = "com.rspace.rspaceimgmetadata.microservice.repository")
@SpringBootApplication
public class ImageMetadataApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageMetadataApplication.class, args);

    }

}
