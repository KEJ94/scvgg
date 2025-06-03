package org.ejkim.scvgg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.ejkim.scvgg")
public class ScvggApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScvggApplication.class, args);
    }

}
