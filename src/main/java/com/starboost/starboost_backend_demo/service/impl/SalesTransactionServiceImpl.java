// src/main/java/com/starboost/starboost_backend_demo/service/impl/SalesTransactionServiceImpl.java
package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.SalesTransactionDto;
import com.starboost.starboost_backend_demo.entity.Challenge;
import com.starboost.starboost_backend_demo.entity.SalesTransaction;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.repository.ChallengeRepository;
import com.starboost.starboost_backend_demo.repository.SalesTransactionRepository;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import com.starboost.starboost_backend_demo.service.SalesTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Supports CRUD + challenge‐scoped and seller‐scoped lookups.
 */
@Service
@RequiredArgsConstructor
public class SalesTransactionServiceImpl implements SalesTransactionService {
    private final SalesTransactionRepository      repo;
    private final ChallengeRepository             challRepo;
    private final ChallengeParticipantService     participantService;

    @Override
    public SalesTransactionDto create(SalesTransactionDto dto) {
        SalesTransaction entity = toEntity(dto);
        SalesTransaction saved = repo.save(entity);
        return toDto(saved);
    }

    @Override
    public List<SalesTransactionDto> findAll() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SalesTransactionDto findById(Long id) {
        // correct use of orElseThrow
        SalesTransaction tx = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found: " + id));
        return toDto(tx);
    }

    @Override
    public SalesTransactionDto update(Long id, SalesTransactionDto dto) {
        SalesTransaction existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found: " + id));

        // copy updatable fields
        existing.setPremium(dto.getPremium());
        existing.setProduct(dto.getProduct());
        existing.setContractType(dto.getContractType());
        existing.setTransactionNature(dto.getTransactionNature());
        existing.setSellerId(dto.getSellerId());
        existing.setSellerRole(dto.getSellerRole());
        existing.setAgencyId(dto.getAgencyId());
        existing.setRegionId(dto.getRegionId());
        existing.setSaleDate(dto.getSaleDate());
        existing.setSellerName(dto.getSellerName());

        // update challenge link if present
        if (dto.getChallengeId() != null) {
            Challenge ch = challRepo.findById(dto.getChallengeId())
                    .orElseThrow(() -> new RuntimeException("Challenge not found: " + dto.getChallengeId()));
            existing.setChallenge(ch);
        }

        SalesTransaction saved = repo.save(existing);
        return toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repo.deleteAll();
    }

    @Override
    public List<SalesTransactionDto> findAllByChallengeId(Long challengeId) {
        // uses your custom repository method findAllByChallenge_Id
        return repo.findAllByChallenge_Id(challengeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SalesTransactionDto createForChallenge(Long challengeId, SalesTransactionDto dto) {
        Challenge ch = challRepo.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found: " + challengeId));
        SalesTransaction tx = toEntity(dto);
        tx.setChallenge(ch);
        SalesTransaction saved = repo.save(tx);
        return toDto(saved);
    }

    @Override
    public List<SalesTransactionDto> findByChallengeAndRole(Long challengeId, Role role) {
        List<Long> sellerIds = participantService.listParticipantIds(challengeId, role);
        return repo.findAllByChallenge_Id(challengeId).stream()
                .filter(tx -> sellerIds.contains(tx.getSellerId()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalesTransactionDto> findByChallengeAndSellerId(Long challengeId, Long sellerId) {
        return repo.findAllByChallenge_Id(challengeId).stream()
                .filter(tx -> tx.getSellerId().equals(sellerId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalesTransactionDto> findByChallengeAndSellerName(Long challengeId, String sellerName) {
        return repo.findAllByChallenge_Id(challengeId).stream()
                .filter(tx -> tx.getSellerName() != null
                        && tx.getSellerName().equalsIgnoreCase(sellerName))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ─── Helpers ─────────────────────────────────────────────────────

    private SalesTransaction toEntity(SalesTransactionDto d) {
        var builder = SalesTransaction.builder()
                .premium(d.getPremium())
                .product(d.getProduct())
                .contractType(d.getContractType())
                .transactionNature(d.getTransactionNature())
                .sellerId(d.getSellerId())
                .sellerRole(d.getSellerRole())
                .agencyId(d.getAgencyId())
                .regionId(d.getRegionId())
                .saleDate(d.getSaleDate())
                .sellerName(d.getSellerName());

        if (d.getChallengeId() != null) {
            builder.challenge(challRepo.getReferenceById(d.getChallengeId()));
        }
        return builder.build();
    }

    private SalesTransactionDto toDto(SalesTransaction t) {
        return SalesTransactionDto.builder()
                .id(t.getId())
                .premium(t.getPremium())
                .product(t.getProduct())
                .contractType(t.getContractType())
                .transactionNature(t.getTransactionNature())
                .sellerId(t.getSellerId())
                .sellerRole(t.getSellerRole())
                .agencyId(t.getAgencyId())
                .regionId(t.getRegionId())
                .saleDate(t.getSaleDate())
                .sellerName(t.getSellerName())
                .challengeId(t.getChallenge() != null ? t.getChallenge().getId() : null)
                .build();
    }
}
