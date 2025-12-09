package com.handler.excel2word.handlerApi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String area;
    private Set<String> roles;
}
