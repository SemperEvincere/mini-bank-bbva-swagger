package com.bbva.minibank.application.services;

import com.bbva.minibank.application.repository.IUserRepository;
import com.bbva.minibank.application.usecases.user.IUserUseCase;
import com.bbva.minibank.domain.models.enums.ERole;
import com.bbva.minibank.infrastructure.entities.RoleEntity;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.presentation.request.user.CreateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final IUserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByUsername(username)
		                                      .orElseThrow(() -> new UsernameNotFoundException(
				                                      "El usuario " + username + " no existe."));
		
		Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
		                                                               .stream()
		                                                               .map(role -> new SimpleGrantedAuthority(
				                                                               "ROLE_".concat(role.getName()
				                                                                                  .name())))
		                                                               .collect(Collectors.toSet());
		
		return new User(userEntity.getUsername(),
		                userEntity.getPassword(),
		                true,
		                true,
		                true,
		                true,
		                authorities);
	}
	
	
	
}
