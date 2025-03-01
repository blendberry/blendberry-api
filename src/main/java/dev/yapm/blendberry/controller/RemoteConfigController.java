package dev.yapm.blendberry.controller;

import dev.yapm.blendberry.entity.RemoteConfig;
import dev.yapm.blendberry.service.RemoteConfigService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configs")
@CrossOrigin(origins = "*")
public class RemoteConfigController {

    public final RemoteConfigService remoteConfigService;

    @Autowired
    public RemoteConfigController(RemoteConfigService remoteConfigService) {
        this.remoteConfigService = remoteConfigService;
    }

    @PostMapping
    public ResponseEntity<RemoteConfig> saveConfig(@Valid @RequestBody RemoteConfig config){
        return ResponseEntity.ok(remoteConfigService.save(config));
    }

    @GetMapping("/{env}")
    public ResponseEntity<?> fetchConfig(@PathVariable String env, @Nullable @RequestParam String version){
        return remoteConfigService.fetch(env, version)
            .map(ResponseEntity::ok)
            .orElseGet( ()-> ResponseEntity.notFound()
                .build());
    }
}