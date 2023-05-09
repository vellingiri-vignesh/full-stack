package com.divineaura;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/")
    public Greeting greet() {
        return new Greeting("Hello...",
            List.of("Java","GoLang","JavaScript"),
            new Person("Anand", 18));
    }

    record Person(String name, int age) { };

    record Greeting(String message, List<String> favProgrammingLanguages, Person person) { }
}
