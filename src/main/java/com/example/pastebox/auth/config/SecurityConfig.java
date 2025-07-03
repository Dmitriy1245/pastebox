package com.example.pastebox.auth.config;

import com.example.pastebox.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var configuration = new org.springframework.web.cors.CorsConfiguration();
                    // Указываем, с какого адреса разрешены запросы.
                    // Это адрес нашего фронтенда, который запустил Vite.
                    configuration.setAllowedOrigins(java.util.List.of("http://localhost:5173"));

                    // Указываем, какие HTTP-методы разрешены.
                    configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

                    // Указываем, какие заголовки разрешены.
                    // "*" означает все, включая наш "Authorization" заголовок.
                    configuration.setAllowedHeaders(java.util.List.of("*"));

                    // Разрешаем отправку credentials (куки, заголовки авторизации).
                    configuration.setAllowCredentials(true);

                    return configuration;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/auth", "/registration").permitAll()
                        .requestMatchers("/page","/{url}").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))//т.к. в ресте нет сессий через кукки
                .exceptionHandling(exc -> exc.authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(authService).passwordEncoder(passwordEncoder);
        return builder.build();
    }
}