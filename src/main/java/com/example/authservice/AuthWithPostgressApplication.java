package com.example.authservice;

import com.example.authservice.entity.Customer;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import com.example.authservice.repository.CustomerRepository;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
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
		//test1(context);
		RoleRepository roleRepository = context.getBean(RoleRepository.class);
		Role role =  new Role("ROLE_KEY", "ROLE_NAME", "TENANT", true);
		roleRepository.save(role).block();

		Role dbRole = roleRepository.findById(role.getId()).block();
		log.info("DBRole: {}", dbRole);

	}

	private static void test1(ApplicationContext context) {
		CustomerRepository customerRepository = context.getBean(CustomerRepository.class);

		customerRepository.save(Customer.builder().firstName("John").lastName("Doe").build()).block();

		List<Customer> customers = customerRepository.findByLastName("Doe").collectList().block();
		log.info("Found {} customers", customers);

		UserRepository userRepository = context.getBean(UserRepository.class);
		User user = new User("54321", "client", "client");
		log.info("{}", user);
		userRepository.save(user).block();
		log.info("{}", user);

		User dbUser = userRepository.findById(user.getId()).block();
		log.info("{}", dbUser);
	}

}
