package com.firenze.http;

import com.firenze.annotation.Delete;
import com.firenze.annotation.Get;
import com.firenze.annotation.Post;
import com.firenze.annotation.Put;
import java.util.List;

public class HttpMethod {
    public static final List<Class<?>> HTTP_METHOD_ANNOTATION_TYPE_COLLECTION = List.of(Get.class,
            Post.class,
            Put.class,
            Delete.class);
}
