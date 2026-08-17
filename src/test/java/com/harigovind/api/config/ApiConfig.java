package com.harigovind.api.config;

/** Environment-backed configuration for the API test suite. */
public final class ApiConfig {
    private static final String DEFAULT_BASE_URL = "https://restful-booker.herokuapp.com";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password123";
    private static final long DEFAULT_RESPONSE_TIME_MS = 10_000L;

    private ApiConfig() {
    }

    public static String baseUrl() {
        return read("API_BASE_URL", DEFAULT_BASE_URL);
    }

    public static String username() {
        return read("API_USERNAME", DEFAULT_USERNAME);
    }

    public static String password() {
        return read("API_PASSWORD", DEFAULT_PASSWORD);
    }

    public static long responseTimeLimitMs() {
        String rawValue = read("API_RESPONSE_TIME_MS", String.valueOf(DEFAULT_RESPONSE_TIME_MS));
        try {
            long value = Long.parseLong(rawValue);
            if (value <= 0) {
                throw new IllegalArgumentException("API_RESPONSE_TIME_MS must be positive");
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("API_RESPONSE_TIME_MS must be a number", exception);
        }
    }

    private static String read(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }
}
