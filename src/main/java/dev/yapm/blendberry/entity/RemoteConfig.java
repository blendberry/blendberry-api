package dev.yapm.blendberry.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Map;

/**
 * Represents a remotely managed configuration for a specific app, environment, and version.
 * <p>
 * This entity is designed to be stored in MongoDB and is the core data structure of the BlendBerry configuration
 * system.
 * <p>
 * The system is designed to be auto-hosted, providing a minimal yet flexible backend
 * to serve dynamic app configurations.
 * <p>
 * Dates are stored as UTC-based Instants to ensure correct synchronization
 * across distributed systems, regardless of local time zones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "configs")
@CompoundIndex(name = "appEnvVersionIndex", def = "{'appId': 1, 'env': 1, 'version': 1}", unique = true)
public class RemoteConfig {

    /**
     * Unique identifier for this configuration document (MongoDB internal ID).
     */
    @Id
    @JsonIgnore
    private String id;

    /**
     * Application identifier. This links the configuration to a specific app.
     * Used for minimal multi-tenancy support.
     */
    @Indexed @NotNull
    private String appId;

    /**
     * Environment name (e.g., 'staging', 'production', 'qa').
     * This allows separate configurations per environment.
     */
    @Indexed @NotNull
    private String env;

    /**
     * App version string (e.g., '1.0.0').
     * Used to optionally serve version-specific configurations.
     */
    @Indexed @NotNull
    private String version;

    /**
     * A generic map of key-value pairs representing the actual configuration data.
     * Values can be strings, booleans, numbers, or nested structures.
     */
    private Map<String, Object> configs;

    /**
     * Timestamp representing when this configuration was first created.
     * Stored in UTC (ISO-8601) for consistency.
     * Used to resolve the latest version in a specific environment.
     */
    private Instant creationDate = Instant.now();

    /**
     * Timestamp representing the last time this configuration was modified.
     * Stored in UTC (ISO-8601) for consistency.
     * Used to determine if client-side data needs to be updated.
     */
    private Instant lastModDate = Instant.now();
}