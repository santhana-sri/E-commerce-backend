package com.hex.ecommerce.config;

import com.hex.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securedFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Public signup endpoint
                        .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                        
                        // Authentication endpoints
                        .requestMatchers(HttpMethod.GET, "/api/auth/login").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/auth/public/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/user/**").authenticated()
                        
                        // Role-based endpoints
                        .requestMatchers(HttpMethod.GET, "/api/auth/admin/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/auth/customer/**").hasAnyAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/auth/vendor/**").hasAnyAuthority("ROLE_VENDOR")
                        
                        // Product endpoints - different access levels
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll() // Anyone can view products
                        .requestMatchers(HttpMethod.POST, "/api/products").hasAnyAuthority("ROLE_VENDOR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyAuthority("ROLE_VENDOR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyAuthority("ROLE_VENDOR", "ROLE_ADMIN")
                        
                        // Vendor endpoints
                        .requestMatchers(HttpMethod.GET, "/api/vendors/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/vendors").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/vendors/**").hasAnyAuthority("ROLE_VENDOR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/vendors/**").hasAuthority("ROLE_ADMIN")
                        
                        // Customer endpoints
                        .requestMatchers(HttpMethod.GET, "/api/customers/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/customers").hasAuthority("ROLE_ADMIN")
                        
                        // Purchase endpoints
                        .requestMatchers(HttpMethod.POST, "/api/purchases").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/purchases/customer/**").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/purchases/product/**").hasAnyAuthority("ROLE_VENDOR", "ROLE_ADMIN")
                        
                        // All other requests require authentication
                        .anyRequest().authenticated()
                );
        
        // Use HTTP Basic Authentication (like TRS project)
        http.httpBasic(Customizer.withDefaults());
        
        return http.build();
    }
}