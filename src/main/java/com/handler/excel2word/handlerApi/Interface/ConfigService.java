package com.handler.excel2word.handlerApi.Interface;

import com.handler.excel2word.handlerApi.entity.Config;

import java.util.List;

public interface ConfigService {

    Config create(Config config);

    Config update(Long id, Config config);

    void delete(Long id);

    Config findById(Long id);

    Config findByKey(String key);

    List<Config> findByName(String name);

    List<Config> findAllConfigs();

    String authGoogle(String vsCode);

    Config findActiveByNameAndKey(String name, String key);
}
