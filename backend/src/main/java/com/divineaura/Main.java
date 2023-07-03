package com.divineaura;

import com.divineaura.customer.Customer;
import com.divineaura.customer.CustomerRepository;
import com.divineaura.customer.Gender;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        Foo foo = applicationContext.getBean(Foo.class);
        System.out.println(foo.bar());
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository,
                             PasswordEncoder passwordEncoder) {
        Random random = new Random();
        Faker fakerCustOne = new Faker();
        int age = random.nextInt(14, 80);
        Gender gender = (age%2 ==0) ? Gender.FEMALE : Gender.FEMALE;
        Customer custOne = new Customer( fakerCustOne.name().fullName(),
            fakerCustOne.internet().safeEmailAddress(),
            passwordEncoder.encode(UUID.randomUUID().toString()), age,
            gender);
        List<Customer> customers = List.of(custOne);
        return args -> {
            customerRepository.saveAll(customers);
        };
    }

    /**
     *
     * Below piece of snippet is for learning bean
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Foo getFoo() {
        System.out.println("Bar Bean loading...");
        return new Foo("Bar");
    }
    record Foo(String bar) { }
    private static void printBeans(ConfigurableApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }


}
