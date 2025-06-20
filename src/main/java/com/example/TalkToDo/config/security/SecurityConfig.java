package com.example.TalkToDo.config.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(
        Arrays.asList("https://*.kro.kr", "http://localhost:3000", "http://localhost:3001"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "Origin", "User-Agent",
        "DNT", "Cache-Control", "X-Mx-ReqToken", "Keep-Alive", "X-Requested-With", "If-Modified-Since"));
    configuration.setAllowCredentials(true);
    configuration.setExposedHeaders(Arrays.asList("Authorization"));
    configuration.setMaxAge(1728000L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
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

    // CORS 설정
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()));

    // 경로별 인가 설정
    http
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll());

    // 세션 설정
    http
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.addFilterAt(new LoginFilter(jwtUtil, authenticationManager(authenticationConfiguration)),
        UsernamePasswordAuthenticationFilter.class);

    http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

    return http.build();
  }
}
