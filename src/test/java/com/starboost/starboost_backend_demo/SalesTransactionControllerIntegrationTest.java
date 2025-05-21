package com.starboost.starboost_backend_demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starboost.starboost_backend_demo.controller.SalesTransactionController;
import com.starboost.starboost_backend_demo.dto.SalesTransactionDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.exception.GlobalExceptionHandler;
import com.starboost.starboost_backend_demo.service.SalesTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for SalesTransactionController endpoints.
 */
@ExtendWith(MockitoExtension.class)
public class SalesTransactionControllerIntegrationTest {

    private MockMvc mvc;

    @Mock
    private SalesTransactionService transactionService;

    @InjectMocks
    private SalesTransactionController controller;

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
    void createValidationError() throws Exception {
        SalesTransactionDto dto = new SalesTransactionDto(); // missing required

        mvc.perform(post("/api/challenges/{id}/sales", 1)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void getAllTransactions_returnsList() throws Exception {
        Long challengeId = 1L;
        SalesTransactionDto dto = new SalesTransactionDto();
        dto.setId(5L);
        dto.setPremium(1000.0);
        dto.setSellerId(10L);
        dto.setSellerRole(Role.AGENT);
        dto.setSaleDate(LocalDateTime.now());
        dto.setSellerName("Seller");
        dto.setChallengeId(challengeId);

        when(transactionService.findAllByChallengeId(challengeId)).thenReturn(List.of(dto));

        mvc.perform(get("/api/challenges/{id}/sales", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].sellerName").value("Seller"));
    }
}

