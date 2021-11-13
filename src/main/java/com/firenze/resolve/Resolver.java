package com.firenze.resolve;

public interface Resolver {
    Resolver resolve(String requestPath, String requestMethod);
}
