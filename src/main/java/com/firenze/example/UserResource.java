package com.firenze.example;

import com.firenze.annotation.GET;
import com.firenze.annotation.Path;
import com.firenze.annotation.QueryParam;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
public class UserResource {
    @GET
    public List<String> users(@QueryParam String name, @QueryParam List<String> ids) {
        return ids.stream().map(id -> name + id).collect(Collectors.toList());
    }
}
