package com.firenze.resource;

import com.firenze.annotation.Delete;
import com.firenze.annotation.Get;
import com.firenze.annotation.Path;
import com.firenze.annotation.PathParam;
import com.firenze.annotation.Post;
import com.firenze.annotation.Put;
import com.firenze.annotation.QueryParam;
import com.firenze.annotation.RequestBody;
import com.firenze.model.Team;
import java.util.List;

@Path("/teams")
public class TeamResource {

    @Get
    public List<Team> getTeams(@QueryParam String name) {
        return List.of(new Team(1L, name));
    }

    @Path("/{id}")
    @Get
    public Team getTeamById(@PathParam Long id) {
        return new Team(id, "A");
    }

    @Path("/{id}")
    public UserResource userResource() {
        return new UserResource();
    }

    @Post
    public Team createTeam(@RequestBody Team team) {
        return team;
    }

    @Path("/{id}")
    @Put
    public Team updateTeam(@PathParam Long id) {
        return new Team(id, "A1");
    }

    @Path("/{id}")
    @Delete
    public void deleteTeam(@PathParam Long id) {

    }
}
