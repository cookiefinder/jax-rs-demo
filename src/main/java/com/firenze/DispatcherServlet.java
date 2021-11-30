package com.firenze;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firenze.http.HttpUtils;
import com.firenze.resolve.Resolver;
import com.firenze.resolve.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {
    private final List<Resolver> resolvers;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DispatcherServlet(List<Resolver> resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        handle(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        handle(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        handle(req, resp);
    }

    public void handle(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Resolver matchedResolver = resolvers.stream()
                    .map(resolver -> {
                        Resolver resolve = resolver.resolve(req.getRequestURI(), req.getMethod());
                        if (resolve instanceof Response) {
                            return resolve;
                        }
                        return null;
                    }).filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Not found request handler: " + req.getMethod() + ":[\"" + req.getRequestURI() + "\"]"));
            Response matchedResponse = (Response) matchedResolver;
            Method method = matchedResponse.getMethod();
            Object invokedResult = method.invoke(matchedResponse.getSubject(), HttpUtils.getInvokedMethodParams(method,
                    matchedResponse.getHttpPath(),
                    req));
            HttpUtils.ok(resp, objectMapper.writeValueAsString(invokedResult));
        } catch (Exception anyEx) {
            String errMsg;
            if (anyEx instanceof InvocationTargetException) {
                errMsg = ((InvocationTargetException) anyEx).getTargetException().getMessage();
            } else {
                errMsg = anyEx.getMessage();
            }
            System.out.println(errMsg);
            HttpUtils.error(resp, errMsg);
        }
    }
}