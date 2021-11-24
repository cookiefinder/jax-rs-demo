package com.firenze.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firenze.annotation.PathParam;
import com.firenze.annotation.QueryParam;
import com.firenze.annotation.RequestBody;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpUtils {
    public static Object[] getInvokedMethodParams(Method method,
                                                  String path,
                                                  HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        return Arrays.stream(method.getParameters()).map(parameter -> {
            if (Objects.nonNull(parameter.getAnnotation(QueryParam.class))) {
                String paramValue = request.getParameter(parameter.getName());
                if (Collection.class.isAssignableFrom(parameter.getType())) {
                    String[] splitParams = paramValue.split(",");
                    return Arrays.stream(splitParams).collect(Collectors.toList());
                }
                return paramValue;
            }
            if (Objects.nonNull(parameter.getAnnotation(RequestBody.class))) {
                try {
                    return objectMapper.readValue(request.getInputStream(), parameter.getType());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (Objects.nonNull(parameter.getAnnotation(PathParam.class))) {
                Map<String, String> map = HttpPathMatcher.extractPathVariable(path, request.getRequestURI());
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

    public static void ok(HttpServletResponse resp, String response) throws IOException {
        resp.setHeader("Content-Type", "application/json");
        resp.setStatus(200);
        resp.getWriter().write(response);
    }

    public static void error(HttpServletResponse resp, String error) {
        try {
            resp.setHeader("Content-Type", "application/json");
            resp.setStatus(500);
            resp.getWriter().write(error);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
