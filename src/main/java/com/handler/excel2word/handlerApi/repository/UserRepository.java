package com.handler.excel2word.handlerApi.repository;

import com.handler.excel2word.handlerApi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByLogin(String login);

    @Query(" select distinct u from User u left join fetch u.authorities where u.login = :login")
    Optional<User> findByLoginWithAuthorities(@Param("login") String login);
}
