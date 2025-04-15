package dev.yapm.blendberry.exception;

/**
 * Thrown to indicate that no remote configuration was found for the specified appId, environment, and version.
 *
 * <p>This exception is typically thrown when attempting to fetch a specific configuration that does not exist.</p>
 */
public class RemoteConfigNotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     */
    public RemoteConfigNotFoundException(String appId, String env, String version) {
        super("No configuration found for appId=" + appId + ", env=" + env + ", " + " version=" + version);
    }
}