package com.handler.excel2word.handlerApi.entity;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "config")
@Getter
@Setter
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configId;

    @Column(name = "config_name")
    private String configName;

    @Column(name = "config_key")
    private String configKey;

    @Column(name = "config_value")
    private String configValue;

    @Column(name = "config_type")
    private Integer configType;

    @Column(name = "config_remark")
    private String configRemark;

    @Column(name = "config_sort")
    private Integer configSort;

    @Column(name = "enlarge_memo")
    private String enlargeMemo;

    @Column(name = "enable_status")
    private Integer enableStatus;

    @Column(name = "ascription_type")
    private Integer ascriptionType; // 1: sms, 2: mail

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
}