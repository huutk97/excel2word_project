package com.handler.excel2word.handlerApi.repository;

import com.handler.excel2word.handlerApi.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}