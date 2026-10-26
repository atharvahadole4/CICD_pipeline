package com.example.gitlabcicd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PipelineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homeReturnsGitLabCicdMessage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("GitLab CI/CD home program"));
    }

    @Test
    void startReturnsStartedMessage() throws Exception {
        mockMvc.perform(get("/pipeline/start"))
                .andExpect(status().isOk())
                .andExpect(content().string("CI/CD pipeline started"));
    }

    @Test
    void stopReturnsStoppedMessage() throws Exception {
        mockMvc.perform(get("/pipeline/stop"))
                .andExpect(status().isOk())
                .andExpect(content().string("CI/CD pipeline stopped"));
    }

    @Test
    void holdReturnsOnHoldMessage() throws Exception {
        mockMvc.perform(get("/pipeline/hold"))
                .andExpect(status().isOk())
                .andExpect(content().string("CI/CD pipeline on hold"));
    }

    @Test
    void unknownActionReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/pipeline/restart"))
                .andExpect(status().isBadRequest());
    }
}
