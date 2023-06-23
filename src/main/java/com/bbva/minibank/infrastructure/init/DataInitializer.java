package com.bbva.minibank.infrastructure.init;

import com.bbva.minibank.application.services.UserDetailsServiceImpl;
import com.bbva.minibank.application.services.UserService;
import com.bbva.minibank.domain.models.enums.ERole;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.presentation.request.user.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {
	
	private final UserService userService;
	
	@Autowired
	public DataInitializer(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public void run(ApplicationArguments args) {
		// Crear un usuario ADMIN
		Set<ERole> roles = new HashSet<>();
		roles.add(ERole.valueOf("ADMIN"));
		
		CreateUserRequest adminUserRequest = new CreateUserRequest();
		adminUserRequest.setUsername("admin");
		adminUserRequest.setPassword("admin");
		adminUserRequest.setEmail("admin@example.com");
		adminUserRequest.setRoles(roles);
		
		UserEntity adminUser = userService.createUser(adminUserRequest);
		
		// Realizar otras acciones de inicialización si es necesario
	}
}
