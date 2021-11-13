package com.firenze.resource;

import com.firenze.annotation.Get;
import com.firenze.annotation.Path;
import com.firenze.annotation.PathParam;
import com.firenze.annotation.Post;
import com.firenze.annotation.RequestBody;
import com.firenze.model.User;
import java.util.List;

@Path("/users")
public class UserResource {
    @Get
    public List<User> getAllUsers() {
        return List.of(new User(1L, "testUser"));
    }

    @Path("/{id}")
    @Get
    public User getUserById(@PathParam Long id) {
        return new User(id, "testUser");
    }

    @Post
    public User createUser(@RequestBody User user) {
        return user;
    }
}
