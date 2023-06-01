package com.divineaura;

import com.divineaura.customer.Customer;
import com.divineaura.customer.CustomerRepository;
import java.util.List;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);
//        printBeans(applicationContext);
        Foo foo = applicationContext.getBean(Foo.class);
        System.out.println(foo.bar());
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        Customer anand = new Customer( "Anand" , "anand@hotmail.com", 18);
        Customer saki = new Customer( "Saki" , "saki@hotmail.com", 16);
        List<Customer> customers = List.of(anand, saki);
        return args -> {
            customerRepository.saveAll(customers);
        };
    }

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
