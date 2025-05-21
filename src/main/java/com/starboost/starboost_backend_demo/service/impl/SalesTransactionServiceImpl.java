package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.SalesTransactionDto;
import com.starboost.starboost_backend_demo.entity.Challenge;
import com.starboost.starboost_backend_demo.entity.SalesTransaction;
import com.starboost.starboost_backend_demo.repository.ChallengeRepository;
import com.starboost.starboost_backend_demo.repository.SalesTransactionRepository;
import com.starboost.starboost_backend_demo.service.SalesTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesTransactionServiceImpl implements SalesTransactionService {
    private final SalesTransactionRepository repo;
    private final ChallengeRepository        challRepo;

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
        SalesTransaction tx = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found: " + id));
        return toDto(tx);
    }

    @Override
    public SalesTransactionDto update(Long id, SalesTransactionDto dto) {
        SalesTransaction existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found: " + id));

        // copy all updatable fields
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

        // if challengeId present, update link
        if (dto.getChallengeId() != null) {
            Challenge chall = challRepo.findById(dto.getChallengeId())
                    .orElseThrow(() -> new RuntimeException("Challenge not found: " + dto.getChallengeId()));
            existing.setChallenge(chall);
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
        return repo.findAllByChallengeId(challengeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SalesTransactionDto createForChallenge(Long challengeId, SalesTransactionDto dto) {
        Challenge chall = challRepo.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found: " + challengeId));
        SalesTransaction tx = toEntity(dto);
        tx.setChallenge(chall);
        SalesTransaction saved = repo.save(tx);
        return toDto(saved);
    }

    // ─── Helpers ───────────────────────────────────────

    private SalesTransaction toEntity(SalesTransactionDto d) {
        SalesTransaction.SalesTransactionBuilder builder = SalesTransaction.builder()
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

        // if challengeId present, link it
        if (d.getChallengeId() != null) {
            Challenge chall = challRepo.getReferenceById(d.getChallengeId());
            builder.challenge(chall);
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