package com.example.TalkToDo.config.security.jwt;

import org.springframework.web.filter.OncePerRequestFilter;

import com.example.TalkToDo.config.security.PrincipalDetails;
import com.example.TalkToDo.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
  private final JWTUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorization = request.getHeader("Authorization");

    if (authorization == null || !authorization.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authorization.split(" ")[1];

    if (jwtUtil.isExpired(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    Long id = jwtUtil.getId(token);
    String username = jwtUtil.getUsername(token);
    String email = jwtUtil.getEmail(token);

    User user = User.builder()
        .id(id)
        .username(username)
        .email(email)
        .build();

    PrincipalDetails principalDetails = new PrincipalDetails(user);

    Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
        principalDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }
}
