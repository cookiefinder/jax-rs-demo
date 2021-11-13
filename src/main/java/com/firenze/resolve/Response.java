package com.firenze.resolve;

import com.firenze.http.HttpPathMatcher;
import java.lang.reflect.Method;
import java.util.Objects;

public class Response implements Resolver {
    private final String httpPath;
    private final String httpMethod;
    private final Method method;
    private final Object subject;

    private Response(String path, String httpMethod, Method method, Object subject) {
        this.httpPath = path;
        this.httpMethod = httpMethod;
        this.method = method;
        this.subject = subject;
    }

    public static Response of(String path, String httpMethod, Method method, Object subject) {
        return new Response(path, httpMethod, method, subject);
    }

    @Override
    public Resolver resolve(String requestPath, String requestMethod) {
        if (HttpPathMatcher.match(this.httpPath, requestPath) && Objects.equals(requestMethod, this.httpMethod)) {
            return this;
        }
        return null;
    }

    public Method getMethod() {
        return method;
    }

    public Object getSubject() {
        return subject;
    }

    public String getHttpPath() {
        return httpPath;
    }
}
