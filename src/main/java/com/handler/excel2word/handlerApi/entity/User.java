package com.handler.excel2word.handlerApi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user") // đúng với DDL bạn đưa
@Getter
@Setter
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @Column(name = "password_hash", length = 60, nullable = false)
    private String passwordHash;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(nullable = false)
    private boolean activated;

    @Column(length = 50)
    private String area;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_name")
    )
    private Set<Authority> authorities = new HashSet<>();

    // getter, setter, add/remove authority...
    // (viết đầy đủ theo IDE generate)
}
