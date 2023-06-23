package com.bbva.minibank.application.services;

import com.bbva.minibank.application.repository.IUserRepository;
import com.bbva.minibank.application.usecases.user.IUserUseCase;
import com.bbva.minibank.domain.models.enums.ERole;
import com.bbva.minibank.infrastructure.entities.RoleEntity;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.presentation.request.user.CreateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserUseCase {
	
	private final PasswordEncoder passwordEncoder;
	private final IUserRepository userRepository;
	@Override
	public void save(UserEntity userEntity) {
		userRepository.save(userEntity);
	}
	
	@Override
	public UserEntity createUser(@Valid CreateUserRequest createUserRequest) {
		Set<RoleEntity> roles = createUserRequest.getRoles()
		                                         .stream()
		                                         .map(role -> RoleEntity.builder()
		                                                                .name(ERole.valueOf(String.valueOf(role)))
		                                                                .build())
		                                         .collect(Collectors.toSet());
		
		UserEntity userEntity = UserEntity.builder()
		                                  .username(createUserRequest.getUsername())
		                                  .password(passwordEncoder.encode(createUserRequest.getPassword()))
		                                  .email(createUserRequest.getEmail())
		                                  .roles(roles)
		                                  .build();
		
		this.save(userEntity);
		return userEntity;
	}
	
	@Override
	public UserEntity findUserById(UUID userId) {
		return userRepository.findUserById(userId);
	}
}
