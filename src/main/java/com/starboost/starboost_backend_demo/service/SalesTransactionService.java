// src/main/java/com/starboost/starboost_backend_demo/service/SalesTransactionService.java
package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.SalesTransactionDto;
import com.starboost.starboost_backend_demo.entity.Role;
import java.util.List;

public interface SalesTransactionService {

    // Create operations
    SalesTransactionDto create(SalesTransactionDto dto);
    SalesTransactionDto createForChallenge(Long challengeId, SalesTransactionDto dto);

    // Read operations
    List<SalesTransactionDto> findAll();
    SalesTransactionDto findById(Long id);
    List<SalesTransactionDto> findAllByChallengeId(Long challengeId);

    // Update operation
    SalesTransactionDto update(Long id, SalesTransactionDto dto);

    // Delete operations
    void deleteById(Long id);
    void deleteAll();

    // New filtering methods
    List<SalesTransactionDto> findByChallengeAndRole(Long challengeId, Role role);
    List<SalesTransactionDto> findByChallengeAndSellerId(Long challengeId, Long sellerId);
    List<SalesTransactionDto> findByChallengeAndSellerName(Long challengeId, String sellerName);
}


