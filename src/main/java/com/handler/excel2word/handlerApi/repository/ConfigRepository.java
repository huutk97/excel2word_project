package com.handler.excel2word.handlerApi.repository;

import com.handler.excel2word.handlerApi.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {

    Optional<Config> findByConfigKey(String configKey);

    List<Config> findByConfigName(String configName);

    List<Config> findByEnableStatus(Integer enableStatus);

    Optional<Config> findByConfigNameAndConfigKeyAndEnableStatus(
            String configName,
            String configKey,
            Integer enableStatus
    );
}
