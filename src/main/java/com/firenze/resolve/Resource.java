package com.firenze.resolve;

import java.util.List;
import java.util.Objects;

public class Resource implements Resolver {
    private final List<Resolver> resolvers;

    private Resource(List<Resolver> resolvers) {
        this.resolvers = resolvers;
    }

    public static Resource of(List<Resolver> resolvers) {
        return new Resource(resolvers);
    }

    @Override
    public Resolver resolve(String requestPath, String requestMethod) {
        return resolvers.stream()
                .map(resolver -> resolver.resolve(requestPath, requestMethod))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
