package com.example.TalkToDo.config.security;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import com.example.TalkToDo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.TalkToDo.entity.User;

@Service
@RequiredArgsConstructor
public class PricipalDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return new PrincipalDetails(user);
  }
}
