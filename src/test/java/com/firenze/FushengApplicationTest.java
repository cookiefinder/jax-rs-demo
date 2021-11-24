package com.firenze;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firenze.model.Team;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FushengApplicationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() throws InterruptedException {
        Thread started = new Thread(() -> FushengApplication.run(FushengApplicationTest.class, new String[]{}));
        started.setDaemon(true);
        started.start();
        // TODO check server is started
        while (!isPortInUse("127.0.0.1", 8080)) {
            TimeUnit.MILLISECONDS.sleep(1);
        }
    }

    @AfterEach
    void tearDown() {
        FushengApplication.showDown();
    }

    @Test
    void test() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/teams/1"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
        Team foundTeam = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        Assertions.assertEquals(foundTeam.getId(), 1);
        Assertions.assertEquals(foundTeam.getName(), "A");
    }

    private boolean isPortInUse(String host, int port) {
        boolean result = false;
        try {
            (new Socket(host, port)).close();
            result = true;
        } catch (IOException ignored) {
        }
        return result;
    }
}
