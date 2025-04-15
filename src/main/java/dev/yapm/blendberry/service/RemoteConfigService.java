package dev.yapm.blendberry.service;

import dev.yapm.blendberry.entity.RemoteConfig;
import dev.yapm.blendberry.exception.RemoteConfigNotFoundException;
import dev.yapm.blendberry.repository.RemoteConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Service class for managing RemoteConfig operations.
 * Provides methods to save, retrieve, and compare configuration records.
 */
@Service
public class RemoteConfigService {

    private final RemoteConfigRepository repository;

    @Autowired
    public RemoteConfigService(RemoteConfigRepository repository) {
        this.repository = repository;
    }

    /**
     * Persists a new or updated configuration entry into the database.
     *
     * @param config the RemoteConfig object to store.
     * @return the saved RemoteConfig object.
     */
    public RemoteConfig save(RemoteConfig config) {
        return repository.save(config);
    }

    /**
     * Finds a specific configuration based on app ID, environment, and version.
     * If version is <code>"latest"</code>, returns the most recent config by creation date for the environment.
     *
     * @param appId the application identifier.
     * @param env the environment (e.g., dev, prod).
     * @param version the configuration version or "latest".
     * @return an {@link Optional} containing the config if found.
     */
    public Optional<RemoteConfig> findOne(String appId, String env, String version) {
        if (Objects.equals(version, "latest"))
            return repository.findTopByAppIdAndEnvOrderByCreationDateDesc(appId, env);
        return repository.findByAppIdAndEnvAndVersion(appId, env, version);
    }

    /**
     * It is used to ensure that the requested configuration is reachable, as well as whether it needs to be updated.
     *
     * @param appId the application ID.
     * @param env the environment.
     * @param version the version to check.
     * @param lastModDate the client's last modification date (ISO-8601 format).
     * @return One of these two status codes:
     *         <ul>
     *             <li>{@code 0} - (UP_TO_DATE) The client app can still using the local configs.</li>
     *             <li>{@code 1} - (NEEDS_TO_UPDATE) The client app needs to be updated with the latest changes to
     *             that configuration.</li>
     *         </ul>
     * @throws RemoteConfigNotFoundException if none configuration was resolved, it means at least one of the params
     * mismatched.
     */
    public Integer lookup(String appId, String env, String version, String lastModDate) {
        Optional<RemoteConfig> optConfig = repository.findByAppIdAndEnvAndVersion(appId, env, version);
        if (optConfig.isPresent()) {
            RemoteConfig config = optConfig.get();
            Instant storedModDate = config.getLastModDate();
            Instant clientModDate = Instant.parse(lastModDate);
            return storedModDate.equals(clientModDate) ? ConfigStatus.UP_TO_DATE : ConfigStatus.NEEDS_TO_UPDATE;
        } else {
            throw new RemoteConfigNotFoundException(appId, env, version);
        }
    }

    /**
     * Updates the configuration map for a given remote configuration.
     *
     * <p>This method only modifies the {@code configs} and {@code lastModDate} fields of the
     * {@link RemoteConfig} identified by {@code appId}, {@code env}, and {@code version}.
     * If no matching config is found, a {@link RemoteConfigNotFoundException} is thrown.
     *
     * @param appId      the application ID associated with the config.
     * @param env        the environment (e.g., dev, prod) of the config.
     * @param version    the version identifier of the config.
     * @param newConfigs the new configuration map to store.
     * @return the updated {@link RemoteConfig} object.
     * @throws RemoteConfigNotFoundException if the config is not found.
     */
    public RemoteConfig updateConfigs(String appId, String env, String version, Map<String, Object> newConfigs) {
        RemoteConfig config = repository.findByAppIdAndEnvAndVersion(appId, env, version)
            .orElseThrow(() -> new RemoteConfigNotFoundException(appId, env, version));

        config.setConfigs(newConfigs);
        config.setLastModDate(Instant.now());
        return repository.save(config);
    }

    /**
     * Deletes a remote configuration from the database and returns the deleted entity.
     *
     * <p>Looks up a {@link RemoteConfig} by {@code appId}, {@code env}, and {@code version},
     * removes it from the repository, and returns it. If not found, throws
     * {@link RemoteConfigNotFoundException}.
     *
     * @param appId   the application ID of the config to delete.
     * @param env     the environment of the config to delete.
     * @param version the version of the config to delete.
     * @return the deleted {@link RemoteConfig} entity.
     * @throws RemoteConfigNotFoundException if the config does not exist.
     */
    public RemoteConfig deleteConfig(String appId, String env, String version) {
        RemoteConfig config = repository.findByAppIdAndEnvAndVersion(appId, env, version)
            .orElseThrow(() -> new RemoteConfigNotFoundException(appId, env, version));
        repository.delete(config);
        return config;
    }
}