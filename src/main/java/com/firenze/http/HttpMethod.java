package com.firenze.http;

import com.firenze.annotation.DELETE;
import com.firenze.annotation.GET;
import com.firenze.annotation.POST;
import com.firenze.annotation.PUT;
import java.util.List;

public class HttpMethod {
    public static final List<Class<?>> HTTP_METHOD_ANNOTATION_TYPE_COLLECTION = List.of(GET.class,
            POST.class,
            PUT.class,
            DELETE.class);
}
