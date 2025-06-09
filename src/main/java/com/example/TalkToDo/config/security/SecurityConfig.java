package com.example.TalkToDo.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.TalkToDo.config.security.jwt.JWTFilter;
import com.example.TalkToDo.config.security.jwt.JWTUtil;
import com.example.TalkToDo.config.security.jwt.LoginFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JWTUtil jwtUtil;
  private final AuthenticationConfiguration authenticationConfiguration;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // csrf disable
    http
        .csrf(auth -> auth.disable());

    // Form 인증방식 disable
    http
        .formLogin((auth) -> auth.disable());

    // http basic 인증방식 disable
    http
        .httpBasic((auth) -> auth.disable());

    // 경로별 인가 설정
    http
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll());

    // 세션 설정
    http
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // CORS 설정
    http
        .cors(corsConfigurer -> corsConfigurer.configurationSource(request -> {
          CorsConfiguration config = new CorsConfiguration();
          // ✅ 방법 1: 구체적인 오리진 지정
          // config.setAllowedOrigins(corsAllowedOrigins);
          // ✅ 방법 2: 패턴 사용
          config.setAllowedOriginPatterns(List.of("*"));
          config.setAllowedMethods(List.of("*"));
          config.setAllowCredentials(true);
          config.setAllowedHeaders(List.of("*"));
          config.setExposedHeaders(List.of("*"));
          return config;
        }));

    http.addFilterAt(new LoginFilter(jwtUtil, authenticationManager(authenticationConfiguration)),
        UsernamePasswordAuthenticationFilter.class);

    http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

    return http.build();
  }
}
