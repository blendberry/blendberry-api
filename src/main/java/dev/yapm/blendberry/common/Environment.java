package dev.yapm.blendberry.common;

/**
 *  Defines the supported environments.
 */
public class Environment {

    /**
     *  Local environment. It can be used for testing in the local environment of a specific developer or team.
     */
    public static String local = "local";

    /**
     *  Development environment. This is common for internal testing, where developers make quick changes and tests.
     */
    public static String dev = "dev";

    /**
     *  Environment for automated testing or QA.
     */
    public static String test = "test";

    /**
     *  Integration or pre-production testing environment. This is where features are tested in an environment that
     *  replicates the production environment as closely as possible.
     */
    public static String staging = "staging";

    /**
     *  Production environment. The actual environment where end users interact with the application.
     */
    public static String prod = "prod";
}
