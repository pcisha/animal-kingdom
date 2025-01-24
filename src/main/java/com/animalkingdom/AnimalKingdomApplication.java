package com.animalkingdom;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnimalKingdomApplication {

    public static void main(String[] args) {
        System.out.println("Starting Animal Kingdom Application at " + LocalDateTime.now());
        SpringApplication.run(AnimalKingdomApplication.class, args);
    }

}
