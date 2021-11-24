package com.firenze.resource;

import com.firenze.annotation.DELETE;
import com.firenze.annotation.GET;
import com.firenze.annotation.Path;
import com.firenze.annotation.PathParam;
import com.firenze.annotation.POST;
import com.firenze.annotation.PUT;
import com.firenze.annotation.QueryParam;
import com.firenze.annotation.RequestBody;
import com.firenze.model.Team;
import java.util.List;

@Path("/teams")
public class TeamResource {

    @GET
    public List<Team> getTeams(@QueryParam String name) {
        return List.of(new Team(1L, name));
    }

    @Path("/{id}")
    @GET
    public Team getTeamById(@PathParam Long id) {
        return new Team(id, "A");
    }

    @Path("/{id}")
    public UserResource userResource() {
        return new UserResource();
    }

    @POST
    public Team createTeam(@RequestBody Team team) {
        return team;
    }

    @Path("/{id}")
    @PUT
    public Team updateTeam(@PathParam Long id) {
        return new Team(id, "A1");
    }

    @Path("/{id}")
    @DELETE
    public void deleteTeam(@PathParam Long id) {

    }
}
