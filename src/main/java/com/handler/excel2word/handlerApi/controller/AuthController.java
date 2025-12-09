package com.handler.excel2word.handlerApi.controller;

import com.handler.excel2word.handlerApi.components.JwtTokenProvider;
import com.handler.excel2word.handlerApi.dto.JwtResponse;
import com.handler.excel2word.handlerApi.dto.LoginRequest;
import com.handler.excel2word.handlerApi.dto.RegisterRequest;
import com.handler.excel2word.handlerApi.repository.UserRepository;
import com.handler.excel2word.handlerApi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;


    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        String jwt = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        // ✅ 1. Validate input
        if (req.getLogin() == null || req.getLogin().isEmpty()) {
            return ResponseEntity.badRequest().body("Login is required");
        }
        if (req.getPassword() == null || req.getPassword().length() < 6) {
            return ResponseEntity
                    .badRequest()
                    .body("Password must be at least 6 characters");
        }

        // ✅ 2. Check duplicate login
        if (userRepository.findOneByLogin(req.getLogin()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Login already exists");
        }
        authService.register(req);
        return ResponseEntity.ok("Register success");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {

        // Clear security context
        SecurityContextHolder.clearContext();

        // Nếu bạn có refresh-token → revoke tại đây

        return ResponseEntity.ok().build();
    }
}