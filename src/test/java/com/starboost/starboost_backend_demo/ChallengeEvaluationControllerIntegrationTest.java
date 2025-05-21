// src/test/java/com/starboost/starboost_backend_demo/ChallengeEvaluationControllerIntegrationTest.java
package com.starboost.starboost_backend_demo;

import com.starboost.starboost_backend_demo.controller.ChallengeEvaluationController;
import com.starboost.starboost_backend_demo.dto.WinnerDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.exception.GlobalExceptionHandler;
import com.starboost.starboost_backend_demo.service.ChallengeEvaluationService;
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
 * Integration tests for ChallengeEvaluationController endpoints.
 */
@ExtendWith(MockitoExtension.class)
public class ChallengeEvaluationControllerIntegrationTest {

    private MockMvc mvc;

    @Mock
    private ChallengeEvaluationService evaluationService;

    @InjectMocks
    private ChallengeEvaluationController controller;

    @BeforeEach
    void setUp() {
        // Enable @Valid support in standalone MockMvc
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void listWinners_returnsList() throws Exception {
        Long challengeId = 1L;

        // Your DTO has setRoleCategory, not setRole
        WinnerDto dto = new WinnerDto();
        dto.setUserId(55L);
        dto.setRoleCategory(Role.COMMERCIAL);
        dto.setRewardAmount(250.0);

        when(evaluationService.listWinners(challengeId))
                .thenReturn(List.of(dto));

        mvc.perform(get("/api/challenges/{id}/winners", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(55))
                .andExpect(jsonPath("$[0].roleCategory").value("COMMERCIAL"))
                .andExpect(jsonPath("$[0].rewardAmount").value(250.0));
    }

    @Test
    void listWinnersByRole_filtersZeroRewards() throws Exception {
        Long challengeId = 1L;
        String rolePath = "AGENT";

        WinnerDto zeroDto = new WinnerDto();
        zeroDto.setUserId(99L);
        zeroDto.setRoleCategory(Role.AGENT);
        zeroDto.setRewardAmount(0.0);

        when(evaluationService.listWinnersByRole(challengeId, Role.AGENT))
                .thenReturn(List.of(zeroDto));

        mvc.perform(get("/api/challenges/{id}/winners/{role}", challengeId, rolePath))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
