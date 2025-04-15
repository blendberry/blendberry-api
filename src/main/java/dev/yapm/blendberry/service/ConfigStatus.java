package dev.yapm.blendberry.service;

/**
 * Represents the status of a remote configuration compared to the client's version.
 *
 * <p>This class holds static constants to indicate whether the client's configuration is up to date
 * or needs to be updated.</p>
 */
public class ConfigStatus {

    /**
     * Indicates that the remote configuration has not changed and the client is up-to-date.
     */
    public static final int UP_TO_DATE = 0;

    /**
     * Indicates that the remote configuration has changed and the client needs to update.
     */
    public static final int NEEDS_TO_UPDATE = 1;

    private ConfigStatus() {}
}