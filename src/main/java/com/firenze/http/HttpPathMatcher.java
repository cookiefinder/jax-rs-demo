package com.firenze.http;

import java.util.Map;
import org.springframework.util.AntPathMatcher;

public class HttpPathMatcher {
    public static boolean match(String pattern, String url) {
        return new AntPathMatcher().match(pattern, url);
    }

    public static Map<String, String> extractPathVariable(String pattern, String url) {
        return new AntPathMatcher().extractUriTemplateVariables(pattern, url);
    }
}
