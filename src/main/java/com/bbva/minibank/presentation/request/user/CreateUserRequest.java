package com.bbva.minibank.presentation.request.user;

import com.bbva.minibank.domain.models.enums.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	private Set<ERole> roles;
}
