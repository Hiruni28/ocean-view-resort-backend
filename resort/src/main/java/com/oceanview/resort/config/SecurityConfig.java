package com.oceanview.resort.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // disable CSRF (React frontend)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // allow API access
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults()); // keep basic config stable

        return http.build();
    }
}
