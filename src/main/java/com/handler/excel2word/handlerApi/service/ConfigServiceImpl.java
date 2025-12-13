package com.handler.excel2word.handlerApi.service;

import com.handler.excel2word.core.utils.CommonConstants;
import com.handler.excel2word.handlerApi.entity.Config;
import com.handler.excel2word.googleauthenticator.GoogleAuth;
import com.handler.excel2word.handlerApi.Interface.ConfigService;
import com.handler.excel2word.handlerApi.repository.ConfigRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    private final ConfigRepository repo;

    public ConfigServiceImpl(ConfigRepository repo) {
        this.repo = repo;
    }

    @Override
    public Config create(Config config) {
        return repo.save(config);
    }

    @Override
    public Config findActiveByNameAndKey(String name, String key) {
        return repo.findByConfigNameAndConfigKeyAndEnableStatus(name, key, 1)
                .orElseThrow(() -> new RuntimeException("Active config not found"));
    }

    @Override
    public Config update(Long id, Config newData) {
        Config exist = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Config not found"));

        exist.setConfigName(newData.getConfigName());
        exist.setConfigKey(newData.getConfigKey());
        exist.setConfigValue(newData.getConfigValue());
        exist.setConfigType(newData.getConfigType());
        exist.setConfigRemark(newData.getConfigRemark());
        exist.setConfigSort(newData.getConfigSort());
        exist.setEnlargeMemo(newData.getEnlargeMemo());
        exist.setEnableStatus(newData.getEnableStatus());
        exist.setAscriptionType(newData.getAscriptionType());

        exist.setUpdatedAt(java.time.LocalDateTime.now());

        return repo.save(exist);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Config findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Config not found"));
    }

    @Override
    public Config findByKey(String key) {
        return repo.findByConfigKey(key)
                .orElseThrow(() -> new RuntimeException("Config not found"));
    }

    @Override
    public List<Config> findByName(String name) {
        return repo.findByConfigName(name);
    }

    @Override
    public List<Config> findAllConfigs() {
        return repo.findAll();
    }

    @Override
    public String authGoogle(String vsCode) {
//        Config config = repo.findByConfigNameAndConfigKeyAndEnableStatus(CommonConstants.SYSTEM_CONFIG, CommonConstants.CONFIG_AUTH_GOOGLE, 1)
//                .orElse(null);
//        if (config == null) {
//            throw new RuntimeException("Google Auth config not found");
//        }
        if (!GoogleAuth.authcode(vsCode, "EUKKIHTNFAIT4ADK").booleanValue()) {
            throw new RuntimeException("Google Auth code error");
        }
        return "done";
    }
}
