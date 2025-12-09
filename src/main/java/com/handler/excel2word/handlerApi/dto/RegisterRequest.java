package com.handler.excel2word.handlerApi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String area;
}
