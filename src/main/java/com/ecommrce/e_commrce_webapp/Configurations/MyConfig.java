package com.ecommrce.e_commrce_webapp.Configurations;

import com.ecommrce.e_commrce_webapp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig {
    // Autowire the UserService, CustomSuccessHandler, and CustomFailureHandler for use in security configuration.
    private final UserService userService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomFailureHandler customFailureHandler;
    @Autowired
    public MyConfig(UserService userService, CustomSuccessHandler customSuccessHandler, CustomFailureHandler customFailureHandler) {
        this.userService = userService;
        this.customSuccessHandler = customSuccessHandler;
        this.customFailureHandler = customFailureHandler;
    }

    // Configures the security filter chain, which defines the security policies for the application.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Configure authorization rules for different URLs.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/login"
                        ,"/categories/**", "/product/**","/view_products/**")
                        .permitAll() // Allow access to these endpoints without authentication.
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/seller/**").hasRole("SELLER")
                        .anyRequest().authenticated())

                // Configure form-based login.
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(customSuccessHandler)
                        .failureHandler(customFailureHandler))
                .logout(config -> config
                        .logoutSuccessUrl("/"))
                // Configure session management.
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .sessionRegistry(sessionRegistry()))

                // Set the UserService as the user details service.
                .userDetailsService(userService)

                // Build and return the configured SecurityFilterChain.
                .build();
    }

    // Defines a bean for encoding passwords using BCrypt, a strong hashing function.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Defines a bean for managing sessions, allowing the application to keep track of active sessions.
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }


}
