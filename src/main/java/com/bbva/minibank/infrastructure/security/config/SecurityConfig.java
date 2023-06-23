package com.bbva.minibank.infrastructure.security.config;

import com.bbva.minibank.infrastructure.security.filters.JwtAuthenticationFilter;
import com.bbva.minibank.infrastructure.security.filters.JwtAuthorizationFilter;
import com.bbva.minibank.infrastructure.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtUtils jwtUtils;
	private final UserDetailsService userDetailsService;
	
	@Bean
	SessionManagementConfigurer<HttpSecurity> securityFilterChain(HttpSecurity httpSecurity,
	                                        AuthenticationManager authenticationManager) throws Exception {
		
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
		jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
		jwtAuthenticationFilter.setFilterProcessesUrl("/user/login");
		
		return httpSecurity
				       .csrf()
				       .disable()
				       .authorizeRequests()
				       .anyRequest()
				       .permitAll()
				       .and()
				       .sessionManagement()
				       .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}
	
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity,
	                                            PasswordEncoder passwordEncoder) throws Exception {
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
		                   .userDetailsService(userDetailsService)
		                   .passwordEncoder(passwordEncoder)
		                   .and()
		                   .build();
	}
}
