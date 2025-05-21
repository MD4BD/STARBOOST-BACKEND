package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.UserDto;
import java.util.List;
import com.starboost.starboost_backend_demo.dto.user.ProfileUpdateDto;

public interface UserService {
    List<UserDto> findAll();
    UserDto findById(Long id);
    UserDto create(UserDto userDto);
    UserDto update(Long id, UserDto userDto);
    void delete(Long id);
    UserDto findByEmail(String email);
    UserDto updateProfile(Long userId, ProfileUpdateDto dto);
}

