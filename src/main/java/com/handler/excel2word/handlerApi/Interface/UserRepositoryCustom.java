package com.handler.excel2word.handlerApi.Interface;

import com.handler.excel2word.handlerApi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findAll();

    User findById(Long id);

    List<User> searchByKeyword(String keyword);

    User save(User user);

    void deleteById(Long id);

    Page<User> searchByKeyword(String keyword, Pageable pageable);
}
