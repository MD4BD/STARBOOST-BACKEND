package com.starboost.starboost_backend_demo;

import com.starboost.starboost_backend_demo.controller.ChallengeParticipantController;
import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.exception.GlobalExceptionHandler;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
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
 * Integration tests for ChallengeParticipantController endpoints.
 */
@ExtendWith(MockitoExtension.class)
public class ChallengeParticipantControllerIntegrationTest {

    private MockMvc mvc;

    @Mock
    private ChallengeParticipantService participantService;

    @InjectMocks
    private ChallengeParticipantController controller;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void listParticipants_returnsList() throws Exception {
        Long challengeId = 1L;
        ChallengeParticipantDto dto = new ChallengeParticipantDto();
        dto.setParticipantId(100L);
        dto.setUserId(200L);
        dto.setRole(Role.AGENT);

        when(participantService.findByChallengeId(challengeId)).thenReturn(List.of(dto));

        mvc.perform(get("/api/challenges/{id}/participants", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].participantId").value(100))
                .andExpect(jsonPath("$[0].userId").value(200))
                .andExpect(jsonPath("$[0].role").value("AGENT"));
    }

    @Test
    void listParticipants_empty_returnsEmptyArray() throws Exception {
        Long challengeId = 2L;
        when(participantService.findByChallengeId(challengeId)).thenReturn(List.of());

        mvc.perform(get("/api/challenges/{id}/participants", challengeId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
