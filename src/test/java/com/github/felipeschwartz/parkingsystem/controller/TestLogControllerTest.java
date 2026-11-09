package com.github.felipeschwartz.parkingsystem.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestLogControllerTest {

    @Test
    void shouldAnswerThatTheLogsWereGenerated() throws Exception {
        StandaloneMvc.of(new TestLogController()).perform(get("/api/test/v1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logs generated successfully"));
    }
}
