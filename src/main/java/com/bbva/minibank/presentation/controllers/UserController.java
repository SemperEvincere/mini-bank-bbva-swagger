package com.bbva.minibank.presentation.controllers;

import com.bbva.minibank.application.usecases.user.IUserUseCase;
import com.bbva.minibank.domain.models.enums.ERole;
import com.bbva.minibank.infrastructure.entities.RoleEntity;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.presentation.request.user.CreateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	private final IUserUseCase userUseCase;
	
	
	@PostMapping("/createUser")
	public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
		UserEntity userEntity = userUseCase.createUser(createUserRequest);
		
		
		return ResponseEntity.ok(userEntity);
	}
}
