package com.handler.excel2word.configs;

import com.handler.excel2word.handlerApi.entity.User;
import com.handler.excel2word.handlerApi.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByLoginWithAuthorities(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getLogin())
                .password(user.getPasswordHash())
                .authorities(user.getAuthorities()
                        .stream()
                        .map(a -> new SimpleGrantedAuthority(a.getName()))
                        .collect(Collectors.toList()))
                .accountLocked(!user.isActivated())
                .build();
    }
}
