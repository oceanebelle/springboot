package com.oceanebelle.sb;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Using Mock MVC
 */
public class MyRandomHealthTest {

    static final String PATH  = "/status/myrandom";

    @Nested
    @TestPropertySource(properties = "management.health.myrandom.enabled=false")
    @AutoConfigureMockMvc
    @SpringBootTest
    class Disabled {
        @Autowired
        private MockMvc mockMvc;

        @Test
        void givenADisabledIndicator_whenSendingRequest_thenReturns404() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get(PATH))
                    .andExpect(status().isNotFound());
        }
    }


    @Nested
    @TestPropertySource(properties = "management.health.myrandom.enabled=true")
    @AutoConfigureMockMvc
    @SpringBootTest
    class Enabled {
        @Autowired
        private MockMvc mockMvc;

        @Test
        void givenEnabledHealthIndicator_whenSendingRequest_thenReturns200() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get(PATH))
                    .andExpect(status().isOk());
        }
    }


}
