package com.handler.excel2word.handlerApi.controller;

import com.handler.excel2word.handlerApi.entity.Config;
import com.handler.excel2word.handlerApi.Interface.ConfigService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private final ConfigService service;

    public ConfigController(ConfigService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Config create(@RequestBody Config req) {
        return service.create(req);
    }

    @GetMapping("/active")
    public Config getActiveConfig(@RequestParam(name = "name", required = true)  String name, @RequestParam(name = "key", required = true) String key
    ) {
        return service.findActiveByNameAndKey(name, key);
    }

    @PutMapping("/update/{id}")
    public Config update(@PathVariable("id") Long id, @RequestBody Config req) {
        return service.update(id, req);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        service.delete(id);
        return "Deleted";
    }

    @GetMapping("/{id}")
    public Config getOne(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @GetMapping("/key/{key}")
    public Config getByKey(@PathVariable("key") String key) {
        return service.findByKey(key);
    }

    @GetMapping("/name/{name}")
    public List<Config> getByName(@PathVariable("name") String name) {
        return service.findByName(name);
    }

    @GetMapping("/all")
    public List<Config> getAll() {
        return service.findAllConfigs();
    }

    @GetMapping("/authGoogle")
    public String authGoogle(@RequestParam(name = "code", required = true) String code) {
        // Implement Google Authenticator logic here
        return service.authGoogle(code);
    }
}