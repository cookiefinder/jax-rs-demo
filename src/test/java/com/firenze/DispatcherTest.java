package com.firenze;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firenze.http.HttpRequest;
import com.firenze.http.HttpResponse;
import com.firenze.model.Team;
import com.firenze.model.User;
import com.firenze.resolve.Resolver;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherTest {
    private Dispatcher dispatcher;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        ResourceLoader resourceLoader = new ResourceLoader(new ApplicationContextStub());
        List<Resolver> resolvers = resourceLoader.load(getClass());
        dispatcher = new Dispatcher(resolvers);
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnAllTeamsWhenRetrieveTeams() throws JsonProcessingException {
        HttpRequest httpRequest = new HttpRequest("Get", "/teams", Map.of(), null);
        HttpResponse httpResponse = dispatcher.handle(httpRequest);
        Assertions.assertEquals(httpResponse.getStatus(), "OK");
        Assertions.assertEquals(httpResponse.getStatusCode(), 200);
        List<Team> foundTeams = objectMapper.readValue(httpResponse.getData(), new TypeReference<>() {
        });
        Assertions.assertEquals(foundTeams.size(), 1);
        Assertions.assertNull(foundTeams.get(0).getName());
    }

    @Test
    void shouldReturnTeamsWhenRetrieveTeamsByName() throws JsonProcessingException {
        HttpRequest httpRequest = new HttpRequest("Get", "/teams", Map.of("name", "B"), null);
        HttpResponse httpResponse = dispatcher.handle(httpRequest);
        Assertions.assertEquals(httpResponse.getStatus(), "OK");
        Assertions.assertEquals(httpResponse.getStatusCode(), 200);
        List<Team> foundTeams = objectMapper.readValue(httpResponse.getData(), new TypeReference<>() {
        });
        Assertions.assertEquals(foundTeams.size(), 1);
        Assertions.assertEquals(foundTeams.get(0).getName(), "B");
    }

    @Test
    void shouldReturnTeamsWhenRetrieveTeamById() throws JsonProcessingException {
        HttpRequest httpRequest = new HttpRequest("Get", "/teams/1", Map.of(), null);
        HttpResponse httpResponse = dispatcher.handle(httpRequest);
        Assertions.assertEquals(httpResponse.getStatus(), "OK");
        Assertions.assertEquals(httpResponse.getStatusCode(), 200);
        Team foundTeam = objectMapper.readValue(httpResponse.getData(), new TypeReference<>() {
        });
        Assertions.assertEquals(foundTeam.getId(), 1L);
    }

    @Test
    void shouldReturnAllUsersOfTeamWhenRetrieveUserByTeamId() throws JsonProcessingException {
        HttpRequest httpRequest = new HttpRequest("Get", "/teams/1/users", Map.of(), null);
        HttpResponse httpResponse = dispatcher.handle(httpRequest);
        Assertions.assertEquals(httpResponse.getStatus(), "OK");
        Assertions.assertEquals(httpResponse.getStatusCode(), 200);
        List<User> foundUsers = objectMapper.readValue(httpResponse.getData(), new TypeReference<>() {
        });
        Assertions.assertEquals(foundUsers.size(), 1);
        Assertions.assertEquals(foundUsers.get(0).getName(), "testUser");
    }

    @Test
    void shouldReturnUsersOfTeamWhenRetrieveUserByTeamIdAndUserId() throws JsonProcessingException {
        HttpRequest httpRequest = new HttpRequest("Get", "/teams/1/users/1", Map.of(), null);
        HttpResponse httpResponse = dispatcher.handle(httpRequest);
        Assertions.assertEquals(httpResponse.getStatus(), "OK");
        Assertions.assertEquals(httpResponse.getStatusCode(), 200);
        User foundUser = objectMapper.readValue(httpResponse.getData(), new TypeReference<>() {
        });
        Assertions.assertEquals(foundUser.getId(), 1L);
        Assertions.assertEquals(foundUser.getName(), "testUser");
    }

    @Test
    void shouldReturnSavedTeamWhenCreateTeam() throws JsonProcessingException {
        HttpRequest httpRequest = new HttpRequest("Post", "/teams", Map.of(), "{\"id\":1,\"name\":\"NewTeam\"}");
        HttpResponse httpResponse = dispatcher.handle(httpRequest);
        Assertions.assertEquals(httpResponse.getStatus(), "OK");
        Assertions.assertEquals(httpResponse.getStatusCode(), 200);
        Team createdTeam = objectMapper.readValue(httpResponse.getData(), new TypeReference<>() {
        });
        Assertions.assertEquals(createdTeam.getId(), 1L);
        Assertions.assertEquals(createdTeam.getName(), "NewTeam");
    }

    @Test
    void shouldReturnSavedUserWhenCreateUser() throws JsonProcessingException {
        HttpRequest httpRequest = new HttpRequest("Post", "/teams/1/users", Map.of(), "{\"id\":1,\"name\":\"NewUser\"}");
        HttpResponse httpResponse = dispatcher.handle(httpRequest);
        Assertions.assertEquals(httpResponse.getStatus(), "OK");
        Assertions.assertEquals(httpResponse.getStatusCode(), 200);
        User createdUser = objectMapper.readValue(httpResponse.getData(), new TypeReference<>() {
        });
        Assertions.assertEquals(createdUser.getId(), 1L);
        Assertions.assertEquals(createdUser.getName(), "NewUser");
    }

    @Test
    void shouldReturnUpdatedTeamWhenUpdateTeam() throws JsonProcessingException {
        HttpRequest httpRequest = new HttpRequest("Put", "/teams/1", Map.of(), null);
        HttpResponse httpResponse = dispatcher.handle(httpRequest);
        Assertions.assertEquals(httpResponse.getStatus(), "OK");
        Assertions.assertEquals(httpResponse.getStatusCode(), 200);
        Team updatedTeam = objectMapper.readValue(httpResponse.getData(), new TypeReference<>() {
        });
        Assertions.assertEquals(updatedTeam.getId(), 1L);
        Assertions.assertEquals(updatedTeam.getName(), "A1");
    }

    @Test
    void shouldReturnSuccessfullyWhenDeleteTeam() {
        HttpRequest httpRequest = new HttpRequest("Delete", "/teams/1", Map.of(), null);
        HttpResponse httpResponse = dispatcher.handle(httpRequest);
        Assertions.assertEquals(httpResponse.getStatus(), "OK");
        Assertions.assertEquals(httpResponse.getStatusCode(), 200);
    }
}