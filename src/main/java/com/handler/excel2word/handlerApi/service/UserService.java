package com.handler.excel2word.handlerApi.service;

import com.handler.excel2word.core.utils.StringUtil;
import com.handler.excel2word.handlerApi.Interface.UserRepositoryCustom;
import com.handler.excel2word.handlerApi.entity.User;
import com.handler.excel2word.handlerApi.repository.AuthorityRepository;
import com.handler.excel2word.handlerApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepositoryCustom userRepo;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").descending()
        );
        return userRepository.findAll(pageable);
    }

    public Page<User> searchPage(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").descending()
        );

        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll(pageable);
        }

        return userRepo.searchByKeyword(keyword.trim(), pageable);
    }

    public User findOne(Long id) {
        return userRepo.findById(id);
    }

    public User createUser(User user, Set<String> roles) {
        Optional<User> userExisting = userRepository.findByLoginWithAuthorities(user.getLogin());
        if (userExisting.isPresent()) {
            throw new RuntimeException("Login already in use");
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setActivated(true);
        user.setCreatedDate(Instant.now());
        user.setCreatedBy("system");

        user.getAuthorities().clear();
        roles.forEach(r ->
                authorityRepository.findById(r)
                        .ifPresent(user.getAuthorities()::add)
        );

        return userRepo.save(user);
    }

    public User updateUser(Long id, User newData, Set<String> roles) {
        User u = userRepo.findById(id);
        if (u == null) {
            throw new RuntimeException("User not found with id: " + id);
        }

        u.setLogin(newData.getLogin());
        u.setFirstName(newData.getFirstName());
        u.setLastName(newData.getLastName());
        u.setArea(newData.getArea());
        u.setLastModifiedDate(Instant.now());
        u.setLastModifiedBy("system");

        if (StringUtil.isNotBlank(newData.getPasswordHash()) && !newData.getPasswordHash().equalsIgnoreCase(u.getPasswordHash())) {
            u.setPasswordHash(passwordEncoder.encode(newData.getPasswordHash()));
        }

        u.getAuthorities().clear();
        roles.forEach(r ->
                authorityRepository.findById(r)
                        .ifPresent(u.getAuthorities()::add)
        );

        return userRepo.save(u);
    }

    public void changePassword(String username,
                               String oldPassword,
                               String newPassword) {

        User user = userRepository.findOneByLogin(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User getUserLogin() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByLoginWithAuthorities(username).orElse(null);
        if (Objects.isNull(user)) {
            throw new RuntimeException("User not found: " + username);
        }
        return user;
    }

    public User getUserLoginIgnoreException() {
        try {
            Authentication auth = SecurityContextHolder
                    .getContext()
                    .getAuthentication();
            String username = auth.getName();
            User user = userRepository.findByLoginWithAuthorities(username).orElse(null);
            if (Objects.isNull(user)) {
                return null;
            }
            return user;
        } catch (Exception ex) {
            return null;
        }
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}