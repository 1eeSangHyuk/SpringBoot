package com.tjoeun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http.formLogin()
		.loginPage("/users/login")
		.defaultSuccessUrl("/")
//		.usernameParameter("email")
//		.failureUrl("/user/login/error")
		.and()
		.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
		.logoutSuccessUrl("/")
		.invalidateHttpSession(true);

		http.authorizeHttpRequests()
				.mvcMatchers("/**").permitAll()
//				.mvcMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated();
		
//		http.exceptionHandling()
//				.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
