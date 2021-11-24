package com.firenze.resource;

import com.firenze.annotation.GET;
import com.firenze.annotation.POST;
import com.firenze.annotation.Path;
import com.firenze.annotation.PathParam;
import com.firenze.annotation.QueryParam;
import com.firenze.annotation.RequestBody;
import com.firenze.model.User;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/users")
public class UserResource {
    @GET
    public List<User> getAllUsers(@QueryParam List<String> names) {
        return names.stream().map(name -> new User(1L, name)).collect(toList());
    }

    @Path("/{id}")
    @GET
    public User getUserById(@PathParam Long id) {
        return new User(id, "testUser");
    }

    @POST
    public User createUser(@RequestBody User user) {
        return user;
    }
}
