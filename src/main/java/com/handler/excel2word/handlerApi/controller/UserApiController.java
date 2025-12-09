package com.handler.excel2word.handlerApi.controller;

import com.handler.excel2word.handlerApi.dto.ResetPasswordRequest;
import com.handler.excel2word.handlerApi.entity.User;
import com.handler.excel2word.handlerApi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        userService.changePassword(
                username,
                request.getOldPassword(),
                request.getNewPassword()
        );

        return ResponseEntity.ok().build();
    }

    /* ================= GET ONE ================= */
    @GetMapping("/info")
    public User getOne() {
        return userService.getUserLoginIgnoreException();
    }
}
