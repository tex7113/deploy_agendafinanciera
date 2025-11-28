//package com.dalficc_technologies.agendafinanciera.infrastructure.config;
//
//import com.dalficc_technologies.agendafinanciera.infrastructure.security.FirebaseAuthFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//public class SecurityConfig {
//
//    private final FirebaseAuthFilter firebaseAuthFilter;
//
//    public SecurityConfig(FirebaseAuthFilter firebaseAuthFilter) {
//        this.firebaseAuthFilter = firebaseAuthFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // desactivar CSRF si usas REST
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/v1/user").authenticated()
//                        .anyRequest().permitAll()
//                )
//                .addFilterBefore(firebaseAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}
