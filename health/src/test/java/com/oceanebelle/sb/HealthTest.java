package com.oceanebelle.sb;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;


import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Type test
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
public class HealthTest {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @BeforeEach
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/actuator/health/ping");
    }

    @Test
    public void getHello() throws Exception {
        ResponseEntity<String> allResponse = template.getForEntity(new URL("http://localhost:" + port + "/actuator/health").toString(),
                String.class);
        log.info("all health: {}", allResponse.getBody());
        ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        assertThat(response.getBody()).isEqualTo("{\"status\":\"UP\"}");
    }
}
