package com.handler.excel2word.handlerApi.service;

import com.handler.excel2word.handlerApi.dto.RegisterRequest;
import com.handler.excel2word.handlerApi.entity.Authority;
import com.handler.excel2word.handlerApi.entity.User;
import com.handler.excel2word.handlerApi.repository.AuthorityRepository;
import com.handler.excel2word.handlerApi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       AuthorityRepository authorityRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest req) {
        // ✅ 3. Create User entity
        User user = new User();
        user.setLogin(req.getLogin());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setArea(req.getArea());

        user.setActivated(true);
        user.setCreatedDate(Instant.now());
        user.setCreatedBy("register");

        // ✅ 4. Assign ROLE_USER
        Authority roleUser = authorityRepository
                .findById("ROLE_USER")
                .orElseThrow(() ->
                        new RuntimeException("ROLE_USER not found"));

        user.getAuthorities().add(roleUser);

        // ✅ 5. Save
        userRepository.save(user);
    }
}