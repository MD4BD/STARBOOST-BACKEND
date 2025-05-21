// src/main/java/com/starboost/starboost_backend_demo/service/impl/UserServiceImpl.java
package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.UserDto;
import com.starboost.starboost_backend_demo.dto.user.ProfileUpdateDto;
import com.starboost.starboost_backend_demo.entity.Gender;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.entity.User;
import com.starboost.starboost_backend_demo.repository.AgencyRepository;
import com.starboost.starboost_backend_demo.repository.RegionRepository;
import com.starboost.starboost_backend_demo.repository.UserRepository;
import com.starboost.starboost_backend_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toDto(user);
    }

    @Override
    public UserDto create(UserDto dto) {
        // 1) map everything but password
        User user = toEntity(dto);

        // 2) pull raw password from DTO, encode it, set on entity
        String raw = dto.getPassword();
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        user.setPassword(passwordEncoder.encode(raw));

        // 3) save
        User saved = userRepository.save(user);

        // 4) return DTO (without password)
        return toDto(saved);
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setGender(Gender.valueOf(dto.getGender()));
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setRole(Role.valueOf(dto.getRole()));
        existing.setRegistrationNumber(dto.getRegistrationNumber());
        existing.setActive(dto.getActive() != null && dto.getActive()); // persist active/inactive

        if (dto.getAgencyId() != null) {
            existing.setAgency(
                    agencyRepository.findById(dto.getAgencyId())
                            .orElseThrow(() -> new RuntimeException("Agency not found"))
            );
        } else {
            existing.setAgency(null);
        }

        if (dto.getRegionId() != null) {
            existing.setRegion(
                    regionRepository.findById(dto.getRegionId())
                            .orElseThrow(() -> new RuntimeException("Region not found"))
            );
        } else {
            existing.setRegion(null);
        }

        User saved = userRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
            try {
                userRepository.deleteById(id);
               } catch (DataIntegrityViolationException ex) {
                 throw new ResponseStatusException(
                         HttpStatus.CONFLICT,
                         "Cannot delete user with existing references."
                         );
                }
    }

    @Override
    public UserDto findByEmail(String email) {
        return userRepo.findByEmail(email)
                .map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public UserDto updateProfile(Long userId, ProfileUpdateDto dto) {
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 1) Copy new first name / last name
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());

        u.setPhoneNumber(dto.getPhoneNumber());
        u.setDateOfBirth(dto.getDateOfBirth());

        // 3) Handle optional password change
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords must match");
            }
            u.setPassword(encoder.encode(dto.getPassword()));
        }

        // 4) Persist changes
        userRepo.save(u);

        // 5) Return updated UserDto
        return toDto(u);
    }

    // ——————————————————————————————————————————————————————————————————
    // Helper: Entity → DTO (never include password here)
    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().name())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole().name())
                .registrationNumber(user.getRegistrationNumber())
                .agencyId(user.getAgency() != null ? user.getAgency().getId() : null)
                .regionId(user.getRegion() != null ? user.getRegion().getId() : null)
                .agencyName(user.getAgency() != null ? user.getAgency().getName() : null)
                .regionName(user.getRegion() != null ? user.getRegion().getName() : null)
                .active(user.isActive())      // ← include active flag
                .build();
    }

    // Helper: DTO → Entity (do NOT map password here)
    private User toEntity(UserDto dto) {
        User.UserBuilder builder = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .gender(Gender.valueOf(dto.getGender()))
                .dateOfBirth(dto.getDateOfBirth())
                .role(Role.valueOf(dto.getRole()))
                .registrationNumber(dto.getRegistrationNumber());

        if (dto.getAgencyId() != null) {
            builder.agency(agencyRepository.getReferenceById(dto.getAgencyId()));
        }
        if (dto.getRegionId() != null) {
            builder.region(regionRepository.getReferenceById(dto.getRegionId()));
        }
        return builder.build();
    }
}
