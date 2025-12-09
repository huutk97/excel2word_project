package com.handler.excel2word.handlerApi.controller;

import com.handler.excel2word.handlerApi.dto.UserDTO;
import com.handler.excel2word.handlerApi.entity.User;
import com.handler.excel2word.handlerApi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    /* ================= GET ONE ================= */
    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        return userService.findOne(id);
    }

    @GetMapping
    public Page<User> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.findPage(page, size);
    }

    @GetMapping("/search")
    public Page<User> searchPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.searchPage(keyword, page, size);
    }

    /* ================= CREATE ================= */
    @PostMapping
    public User create(@RequestBody UserDTO dto) {
        User u = new User();
        mapDtoToEntity(dto, u);
        return userService.createUser(u, dto.getRoles());
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public User update(@PathVariable Long id,
                       @RequestBody UserDTO dto) {

        User u = new User();
        mapDtoToEntity(dto, u);
        return userService.updateUser(id, u, dto.getRoles());
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /* ================= MAPPER ================= */
    private void mapDtoToEntity(UserDTO dto, User u) {
        u.setLogin(dto.getLogin());
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setArea(dto.getArea());
        u.setPasswordHash(dto.getPassword());
    }
}