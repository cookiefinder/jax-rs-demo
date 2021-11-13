package com.firenze.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firenze.annotation.PathParam;
import com.firenze.annotation.QueryParam;
import com.firenze.annotation.RequestBody;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class HttpRequest {
    private final String requestMethod;
    private final String requestPath;
    private final Map<String, Object> queryParams;
    private final String requestBody;

    public HttpRequest(String method, String path, Map<String, Object> queryParams, String body) {
        this.requestMethod = method;
        this.requestPath = path;
        this.queryParams = queryParams;
        this.requestBody = body;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Object[] getQueryParams(Method method, String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        return Arrays.stream(method.getParameters()).map(parameter -> {
            if (Objects.nonNull(parameter.getAnnotation(QueryParam.class))) {
                return queryParams.get(parameter.getName());
            }
            if (Objects.nonNull(parameter.getAnnotation(RequestBody.class))) {
                try {
                    return objectMapper.readValue(requestBody, parameter.getType());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            if (Objects.nonNull(parameter.getAnnotation(PathParam.class))) {
                Map<String, String> map = HttpPathMatcher.extractPathVariable(path, this.requestPath);
                String paramName = parameter.getName();
                String value = map.get(paramName);
                try {
                    return objectMapper.readValue(value, parameter.getType());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }).toArray();
    }
}