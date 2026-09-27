package org.example.productjdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProductJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductJdbcApplication.class, args);
    }

}
