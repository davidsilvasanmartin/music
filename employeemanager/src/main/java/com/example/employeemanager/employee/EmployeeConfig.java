package com.example.employeemanager.employee;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeConfig {
    @Bean
    CommandLineRunner commandLineRunner(EmployeeService employeeService) {
        return args -> {
            Faker faker = new Faker();

            for (int i = 0; i < 10; i++) {
                employeeService.addEmployee(new Employee(
                        faker.name().firstName() + " " + faker.name().lastName(),
                        faker.internet().emailAddress(),
                        faker.job().title(),
                        faker.phoneNumber().phoneNumber(),
                        faker.internet().image(),
                        faker.job().keySkills()
                ));
            }
        };
    }
}
