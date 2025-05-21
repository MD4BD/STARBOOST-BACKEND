// src/test/java/com/starboost/starboost_backend_demo/LeaderboardControllerIntegrationTest.java
package com.starboost.starboost_backend_demo;

import com.starboost.starboost_backend_demo.controller.ChallengeLeaderboardController;
import com.starboost.starboost_backend_demo.dto.ChallengeLeaderboardDto;
import com.starboost.starboost_backend_demo.exception.GlobalExceptionHandler;
import com.starboost.starboost_backend_demo.service.ChallengeLeaderboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for LeaderboardController endpoints.
 */
@ExtendWith(MockitoExtension.class)
public class LeaderboardControllerIntegrationTest {

    private MockMvc mvc;

    @Mock
    private ChallengeLeaderboardService leaderboardService;

    @InjectMocks
    private ChallengeLeaderboardController controller;

    @BeforeEach
    void setUp() {
        // enable @Valid support
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void getAgents_returnsList() throws Exception {
        Long challengeId = 1L;

        // Build a DTO with the actual fields: userId, name, score
        ChallengeLeaderboardDto dto = ChallengeLeaderboardDto.builder()
                .userId(42L)
                .name("Agent Name")
                .score(100)
                .build();

        when(leaderboardService.agents(challengeId)).thenReturn(List.of(dto));

        mvc.perform(get("/api/challenges/{id}/leaderboard/agents", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(42))
                .andExpect(jsonPath("$[0].name").value("Agent Name"))
                .andExpect(jsonPath("$[0].score").value(100));
    }

    @Test
    void getAgents_empty_returnsEmptyArray() throws Exception {
        Long challengeId = 2L;
        when(leaderboardService.agents(challengeId)).thenReturn(List.of());

        mvc.perform(get("/api/challenges/{id}/leaderboard/agents", challengeId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
