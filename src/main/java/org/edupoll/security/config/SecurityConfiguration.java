package org.edupoll.security.config;

import org.edupoll.security.support.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import lombok.RequiredArgsConstructor;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final JWTAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	SecurityFilterChain finalAppSecurity(HttpSecurity http) throws Exception {
		http.csrf(t-> t.disable());
		http.anonymous(t-> t.disable());
		http.logout(t-> t.disable());
        http.cors(withDefaults());
		
		http.sessionManagement(t-> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		http.authorizeHttpRequests(t-> t
				.requestMatchers("/api/v1/user/private/**") //.hasAuthority("ROLE_VIP")
				.authenticated()
				.anyRequest()
				.permitAll());
		
		http.addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class);
		
		return http.build();
	}
}
