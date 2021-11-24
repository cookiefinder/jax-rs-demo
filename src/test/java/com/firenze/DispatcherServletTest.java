package com.firenze;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firenze.model.Team;
import com.firenze.model.User;
import com.firenze.resolve.Resolver;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class DispatcherServletTest {
    private DispatcherServlet dispatcherServlet;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        ResourceLoader resourceLoader = new ResourceLoader();
        List<Resolver> resolvers = resourceLoader.load(getClass());
        dispatcherServlet = new DispatcherServlet(resolvers);
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnTeamsWhenRetrieveTeamsByName() throws IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        HttpServletRequest req = spy(HttpServletRequest.class);
        doReturn("/teams").when(req).getRequestURI();
        doReturn("GET").when(req).getMethod();
        doReturn(Map.of("name", new String[]{"A"})).when(req).getParameterMap();
        doReturn(null).when(req).getInputStream();
        HttpServletResponse resp = spy(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        doReturn(writer).when(resp).getWriter();
        dispatcherServlet.handle(req, resp);
        verify(resp).setHeader("Content-Type", "application/json");
        verify(resp).setStatus(200);
        verify(writer).write(captor.capture());
        List<Team> foundTeams = objectMapper.readValue(captor.getValue(), new TypeReference<>() {
        });
        Assertions.assertEquals(foundTeams.size(), 1);
        Assertions.assertEquals(foundTeams.get(0).getName(), "A");
    }

    @Test
    void shouldReturnTeamsWhenRetrieveTeamById() throws IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        HttpServletRequest req = spy(HttpServletRequest.class);
        doReturn("/teams/1").when(req).getRequestURI();
        doReturn("GET").when(req).getMethod();
        doReturn(Map.of()).when(req).getParameterMap();
        doReturn(null).when(req).getInputStream();
        HttpServletResponse resp = spy(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        doReturn(writer).when(resp).getWriter();
        dispatcherServlet.handle(req, resp);
        verify(resp).setHeader("Content-Type", "application/json");
        verify(resp).setStatus(200);
        verify(writer).write(captor.capture());
        Team foundTeam = objectMapper.readValue(captor.getValue(), new TypeReference<>() {
        });
        Assertions.assertEquals(foundTeam.getId(), 1L);
    }

    @Test
    void shouldReturnAllUsersOfTeamWhenRetrieveUserByUserNames() throws IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        HttpServletRequest req = spy(HttpServletRequest.class);
        doReturn("/teams/1/users").when(req).getRequestURI();
        doReturn("GET").when(req).getMethod();
        doReturn(Map.of("names", new String[]{"A", "B"})).when(req).getParameterMap();
        doReturn(null).when(req).getInputStream();
        HttpServletResponse resp = spy(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        doReturn(writer).when(resp).getWriter();
        dispatcherServlet.handle(req, resp);
        verify(resp).setHeader("Content-Type", "application/json");
        verify(resp).setStatus(200);
        verify(writer).write(captor.capture());
        List<User> foundUsers = objectMapper.readValue(captor.getValue(), new TypeReference<>() {
        });
        Assertions.assertEquals(foundUsers.size(), 2);
        Assertions.assertEquals(foundUsers.get(0).getName(), "A");
    }

    @Test
    void shouldReturnUsersOfTeamWhenRetrieveUserByTeamIdAndUserId() throws IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        HttpServletRequest req = spy(HttpServletRequest.class);
        doReturn("/teams/1/users/1").when(req).getRequestURI();
        doReturn("GET").when(req).getMethod();
        doReturn(Map.of()).when(req).getParameterMap();
        doReturn(null).when(req).getInputStream();
        HttpServletResponse resp = spy(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        doReturn(writer).when(resp).getWriter();
        dispatcherServlet.handle(req, resp);
        verify(resp).setHeader("Content-Type", "application/json");
        verify(resp).setStatus(200);
        verify(writer).write(captor.capture());
        User foundUser = objectMapper.readValue(captor.getValue(), new TypeReference<>() {
        });
        Assertions.assertEquals(foundUser.getId(), 1L);
        Assertions.assertEquals(foundUser.getName(), "testUser");
    }

    @Test
    void shouldReturnSavedTeamWhenCreateTeam() throws IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        HttpServletRequest req = spy(HttpServletRequest.class);
        doReturn("/teams").when(req).getRequestURI();
        doReturn("POST").when(req).getMethod();
        doReturn(Map.of()).when(req).getParameterMap();
        ServletInputStream servletInputStream = strToServletInputStream("{\"id\":1,\"name\":\"NewTeam\"}");
        doReturn(servletInputStream).when(req).getInputStream();
        HttpServletResponse resp = spy(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        doReturn(writer).when(resp).getWriter();
        dispatcherServlet.handle(req, resp);
        verify(resp).setHeader("Content-Type", "application/json");
        verify(resp).setStatus(200);
        verify(writer).write(captor.capture());
        Team createdTeam = objectMapper.readValue(captor.getValue(), new TypeReference<>() {
        });
        Assertions.assertEquals(createdTeam.getId(), 1L);
        Assertions.assertEquals(createdTeam.getName(), "NewTeam");
    }

    @Test
    void shouldReturnSavedUserWhenCreateUser() throws IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        HttpServletRequest req = spy(HttpServletRequest.class);
        doReturn("/teams/1/users").when(req).getRequestURI();
        doReturn("POST").when(req).getMethod();
        doReturn(Map.of()).when(req).getParameterMap();
        ServletInputStream servletInputStream = strToServletInputStream("{\"id\":1,\"name\":\"NewUser\"}");
        doReturn(servletInputStream).when(req).getInputStream();
        HttpServletResponse resp = spy(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        doReturn(writer).when(resp).getWriter();
        dispatcherServlet.handle(req, resp);
        verify(resp).setHeader("Content-Type", "application/json");
        verify(resp).setStatus(200);
        verify(writer).write(captor.capture());
        User createdUser = objectMapper.readValue(captor.getValue(), new TypeReference<>() {
        });
        Assertions.assertEquals(createdUser.getId(), 1L);
        Assertions.assertEquals(createdUser.getName(), "NewUser");
    }

    @Test
    void shouldReturnUpdatedTeamWhenUpdateTeam() throws IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        HttpServletRequest req = spy(HttpServletRequest.class);
        doReturn("/teams/1").when(req).getRequestURI();
        doReturn("PUT").when(req).getMethod();
        doReturn(Map.of()).when(req).getParameterMap();
        doReturn(null).when(req).getInputStream();
        HttpServletResponse resp = spy(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        doReturn(writer).when(resp).getWriter();
        dispatcherServlet.handle(req, resp);
        verify(resp).setHeader("Content-Type", "application/json");
        verify(resp).setStatus(200);
        verify(writer).write(captor.capture());
        Team updatedTeam = objectMapper.readValue(captor.getValue(), new TypeReference<>() {
        });
        Assertions.assertEquals(updatedTeam.getId(), 1L);
        Assertions.assertEquals(updatedTeam.getName(), "A1");
    }

    @Test
    void shouldReturnSuccessfullyWhenDeleteTeam() throws IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        HttpServletRequest req = spy(HttpServletRequest.class);
        doReturn("/teams/1").when(req).getRequestURI();
        doReturn("DELETE").when(req).getMethod();
        doReturn(Map.of()).when(req).getParameterMap();
        doReturn(null).when(req).getInputStream();
        HttpServletResponse resp = spy(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        doReturn(writer).when(resp).getWriter();
        dispatcherServlet.handle(req, resp);
        verify(resp).setHeader("Content-Type", "application/json");
        verify(resp).setStatus(200);
        verify(writer).write(captor.capture());
        assertEquals(captor.getValue(), "null");
    }

    private ServletInputStream strToServletInputStream(String str) {
        byte[] bytes = str.getBytes(UTF_8);

        return new ServletInputStream() {
            private int lastIndexRetrieved = -1;
            private ReadListener readListener = null;

            @Override
            public int read() throws IOException {
                int i;
                if (!isFinished()) {
                    i = bytes[lastIndexRetrieved+1];
                    lastIndexRetrieved++;
                    if (isFinished() && (readListener != null)) {
                        try {
                            readListener.onAllDataRead();
                        } catch (IOException ex) {
                            readListener.onError(ex);
                            throw ex;
                        }
                    }
                    return i;
                } else {
                    return -1;
                }
            }

            @Override
            public boolean isFinished() {
                return (lastIndexRetrieved == bytes.length - 1);
            }

            @Override
            public boolean isReady() {
                return isFinished();
            }

            @Override
            public void setReadListener(ReadListener listener) {
                this.readListener = listener;
                if (!isFinished()) {
                    try {
                        readListener.onDataAvailable();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                } else {
                    try {
                        readListener.onAllDataRead();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                }
            }
        };
    }
}