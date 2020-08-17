package com.example.authservice;

import com.example.authservice.entity.Customer;
import com.example.authservice.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@Slf4j
@SpringBootApplication
public class AuthWithPostgressApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(AuthWithPostgressApplication.class, args);
		CustomerRepository customerRepository = context.getBean(CustomerRepository.class);

		customerRepository.save(Customer.builder().firstName("John").lastName("Doe").build()).block();

		List<Customer> customers = customerRepository.findByLastName("Doe").collectList().block();
		log.info("Found {} customers", customers);

	}

}
