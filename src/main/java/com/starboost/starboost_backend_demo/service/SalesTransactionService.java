// src/main/java/com/starboost/starboost_backend_demo/service/SalesTransactionService.java
package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.SalesTransactionDto;
import java.util.List;

public interface SalesTransactionService {
    // Create
    SalesTransactionDto create(SalesTransactionDto dto);

    // Read
    List<SalesTransactionDto> findAll();
    SalesTransactionDto       findById(Long id);

    // Update
    SalesTransactionDto       update(Long id, SalesTransactionDto dto);

    // Delete
    void deleteById(Long id);
    void deleteAll();
    List<SalesTransactionDto> findAllByChallengeId(Long challengeId);
    SalesTransactionDto        createForChallenge(Long challengeId, SalesTransactionDto dto);

}
