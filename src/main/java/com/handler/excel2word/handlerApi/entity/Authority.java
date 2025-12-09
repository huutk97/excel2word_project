package com.handler.excel2word.handlerApi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "authority")
@Getter
@Setter
public class Authority implements Serializable {

    @Id
    @Column(length = 50)
    private String name; // ROLE_ADMIN, ROLE_USER

    public Authority() {}

    public Authority(String name) {
        this.name = name;
    }

    // getter, setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
