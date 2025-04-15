package dev.yapm.blendberry.controller;

import dev.yapm.blendberry.entity.RemoteConfig;
import dev.yapm.blendberry.service.RemoteConfigService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller responsible for managing remote configuration entries.
 * <p>Allows storing and retrieving configuration data by environment and version.</p>
 */
@RestController
@RequestMapping("/configs")
public class RemoteConfigController {

    public final RemoteConfigService remoteConfigService;

    @Autowired
    public RemoteConfigController(RemoteConfigService remoteConfigService) {
        this.remoteConfigService = remoteConfigService;
    }

    /**
     * Persists a new remote configuration.
     *
     * @param config The configuration payload to save.
     * @return The saved {@link RemoteConfig}.
     */
    @PostMapping
    public ResponseEntity<RemoteConfig> saveConfig(@Valid @RequestBody RemoteConfig config) {
        return ResponseEntity.ok(remoteConfigService.save(config));
    }

    /**
     * Retrieves a specific configuration by app ID, environment, and version.
     *
     * @param appId   The application identifier (from header).
     * @param env     The environment name (e.g., dev, prod).
     * @param version The specific version of the config to fetch or latest.
     * @return The matching {@link RemoteConfig}, or 404 if not found.
     */
    @GetMapping("{env}")
    public ResponseEntity<RemoteConfig> getConfig(
        @RequestHeader("App-Id") String appId,
        @PathVariable String env,
        @RequestParam @NotBlank String version
    ) {
        return remoteConfigService.findOne(appId, env, version)
            .map(ResponseEntity::ok)
            .orElseGet( ()-> ResponseEntity.notFound()
                .build());
    }

    /**
     * Checks whether the configuration for a given app/env/version has been updated.
     *
     * @param appId        The application ID (from header).
     * @param env          The environment to check.
     * @param version      The configuration version.
     * @param lastModDate  The client's last modification timestamp.
     * @return A status integer: {@code ConfigStatus.UP_TO_DATE} or {@code ConfigStatus.NEEDS_TO_UPDATE}.
     */
    @GetMapping("lookup")
    public ResponseEntity<Integer> lookup(
        @RequestHeader("App-Id") String appId,
        @RequestParam @NotNull String env,
        @RequestParam @NotNull String version,
        @RequestParam @NotNull String lastModDate
    ) {
        return ResponseEntity.ok(remoteConfigService.lookup(appId, env, version, lastModDate));
    }

    /**
     * Updates the configuration map of an existing remote config.
     *
     * <p>This endpoint allows clients to update only the {@code configs} field of an
     * existing config. Requires {@code appId} in the header and {@code env} and {@code version}
     * as path/query parameters.
     *
     * @param appId      the application ID from the request header.
     * @param env        the environment of the configuration (as path variable).
     * @param version    the version of the configuration (as query param).
     * @param newConfigs the new configuration map provided in the request body.
     * @return the updated {@link RemoteConfig}.
     */
    @PatchMapping("{env}")
    public ResponseEntity<RemoteConfig> updateConfigs(
        @RequestHeader("App-Id") String appId,
        @PathVariable String env,
        @RequestParam String version,
        @RequestBody Map<String, Object> newConfigs
    ) {
        RemoteConfig updated = remoteConfigService.updateConfigs(appId, env, version, newConfigs);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a remote configuration and returns the deleted entity.
     *
     * <p>This endpoint removes a {@link RemoteConfig} matching the given {@code appId}, {@code env},
     * and {@code version}. If the config exists, it is deleted and returned in the response.
     *
     * @param appId   the application ID from the request header.
     * @param env     the environment of the config (as path variable).
     * @param version the version to delete (as query param).
     * @return the deleted {@link RemoteConfig}.
     */
    @DeleteMapping("{env}")
    public ResponseEntity<RemoteConfig> deleteConfig(
        @RequestHeader("App-Id") String appId,
        @PathVariable String env,
        @RequestParam String version
    ) {
        RemoteConfig deleted = remoteConfigService.deleteConfig(appId, env, version);
        return ResponseEntity.ok(deleted);
    }
}