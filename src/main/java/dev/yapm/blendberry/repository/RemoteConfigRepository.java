package dev.yapm.blendberry.repository;

import dev.yapm.blendberry.entity.RemoteConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RemoteConfigRepository extends MongoRepository<RemoteConfig, String> {

    Optional<RemoteConfig> findTopByAppIdAndEnvOrderByCreationDateDesc(String appId, String env);

    Optional<RemoteConfig> findByAppIdAndEnvAndVersion(String appId, String env, String version);
}
