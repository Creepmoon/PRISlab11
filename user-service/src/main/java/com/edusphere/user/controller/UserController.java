package com.edusphere.user.controller;

import com.edusphere.common.dto.ApiResponse;
import com.edusphere.user.dto.UserProfileDto;
import com.edusphere.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService service;

    @GetMapping("/{id}")
    public ApiResponse<UserProfileDto> getProfile(@PathVariable UUID id) {
        return ApiResponse.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserProfileDto> updateProfile(@PathVariable UUID id, @RequestBody UserProfileDto dto) {
        dto.setId(id);
        return ApiResponse.ok(service.createOrUpdate(dto));
    }
}
