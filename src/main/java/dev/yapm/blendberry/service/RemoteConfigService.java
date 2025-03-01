package dev.yapm.blendberry.service;

import dev.yapm.blendberry.entity.RemoteConfig;
import dev.yapm.blendberry.repository.RemoteConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RemoteConfigService {

    final private RemoteConfigRepository repository;

    @Autowired
    public RemoteConfigService(RemoteConfigRepository repository) {
        this.repository = repository;
    }

    public RemoteConfig save(RemoteConfig config){
        return repository.save(config);
    }

    public Optional<RemoteConfig> fetch(String env, String version) {
        if (version != null) return repository.findByEnvAndVersion(env, version);
        return repository.findTopByEnvOrderByCreatedAtDesc(env);
    }
}
