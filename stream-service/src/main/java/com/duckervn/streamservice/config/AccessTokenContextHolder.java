package com.duckervn.streamservice.config;

public class AccessTokenContextHolder {

    private static final ThreadLocal<String> accessTokenHolder = new ThreadLocal<>();

    public static String getAccessToken() {
        return accessTokenHolder.get();
    }

    public static void setAccessToken(String accessToken) {
        accessTokenHolder.set(accessToken);
    }

    public static void clearAccessToken() {
        accessTokenHolder.remove();
    }
}
