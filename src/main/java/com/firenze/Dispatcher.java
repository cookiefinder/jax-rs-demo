package com.firenze;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firenze.http.HttpRequest;
import com.firenze.http.HttpResponse;
import com.firenze.resolve.Resolver;
import com.firenze.resolve.Response;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public class Dispatcher {
    private final List<Resolver> resolvers;

    public Dispatcher(List<Resolver> resolvers) {
        this.resolvers = resolvers;
    }

    public HttpResponse handle(HttpRequest request) {
        try {
            Resolver matchedResolver = resolvers.stream()
                    .map(resolver -> {
                        Resolver resolve = resolver.resolve(request.getRequestPath(), request.getRequestMethod());
                        if (resolve instanceof Response) {
                            return resolve;
                        }
                        return null;
                    }).filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Not found request handler: " + request.getRequestMethod() + ":[\"" + request.getRequestPath() + "\"]"));
            Response matchedResponse = (Response) matchedResolver;
            Method method = matchedResponse.getMethod();
            Object invokedResult = method.invoke(matchedResponse.getSubject(), request.getQueryParams(method, matchedResponse.getHttpPath()));
            return HttpResponse.ok(new ObjectMapper().writeValueAsString(invokedResult));
        } catch (Exception anyEx) {
            return HttpResponse.error(anyEx.getMessage());
        }
    }
}