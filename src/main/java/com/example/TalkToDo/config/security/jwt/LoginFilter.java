package com.example.TalkToDo.config.security.jwt;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.TalkToDo.config.security.PrincipalDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Map;
import java.util.Arrays;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final JWTUtil jwtUtil;
  private final AuthenticationManager authenticationManager;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    Authentication token = new UsernamePasswordAuthenticationToken(username.toUpperCase(), password);

    return authenticationManager.authenticate(token);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {
    PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
    String jwtToken = jwtUtil.createJWT(principalDetails.getUser().getId(), principalDetails.getUser().getUsername(), principalDetails.getUser().getEmail());
    response.addHeader("Authorization", "Bearer " + jwtToken);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed) throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
