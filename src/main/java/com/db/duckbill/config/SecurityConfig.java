package com.db.duckbill.config;

import com.db.duckbill.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/acesso-negado", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/usuarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/categorias/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/ativos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/ativos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/ativos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/cotacoes-ativo/**").hasRole("ADMIN")
                .requestMatchers("/app/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .userDetailsService(userDetailsService)
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/app/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/acesso-negado")
            );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
