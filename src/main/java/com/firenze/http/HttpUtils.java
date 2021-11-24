package com.firenze.http;

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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

public class HttpUtils {
    public static Object[] getInvokedMethodParams(Method method,
                                                  String path,
                                                  HttpServletRequest request) {
        AtomicInteger index = new AtomicInteger();
        ObjectMapper objectMapper = new ObjectMapper();
        ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        if (Objects.isNull(parameterNames) || Objects.equals(parameterNames.length, 0)) {
            return new Object[0];
        }
        return Arrays.stream(method.getParameters()).map(parameter -> {
            try {
                String parameterName = parameterNames[index.getAndIncrement()];
                if (Objects.nonNull(parameter.getAnnotation(QueryParam.class))) {
                    String paramValue = request.getParameter(parameterName);
                    if (Collection.class.isAssignableFrom(parameter.getType())) {
                        String[] splitParams = paramValue.split(",");
                        return Arrays.stream(splitParams).collect(Collectors.toList());
                    }
                    return paramValue;
                }
                if (Objects.nonNull(parameter.getAnnotation(RequestBody.class))) {
                    return objectMapper.readValue(request.getInputStream(), parameter.getType());
                }
                if (Objects.nonNull(parameter.getAnnotation(PathParam.class))) {
                    Map<String, String> map = HttpPathMatcher.extractPathVariable(path, request.getRequestURI());
                    String value = map.get(parameterName);
                    return objectMapper.readValue(value, parameter.getType());
                }
                throw new RuntimeException("Not found parameter, parameter name is " + parameterName);
            } catch (Exception anyEx) {
                throw new RuntimeException(anyEx.getMessage());
            }
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
