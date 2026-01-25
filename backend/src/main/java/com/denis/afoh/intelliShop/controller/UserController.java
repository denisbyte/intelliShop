package com.denis.afoh.intelliShop.controller;


import com.denis.afoh.intelliShop.dto.user.UserResponseDto;
import com.denis.afoh.intelliShop.entity.User;
import com.denis.afoh.intelliShop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(mapToResponse(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users.stream().map(this::mapToResponse).toList());

    }



    private UserResponseDto mapToResponse(User user){
        return new UserResponseDto(
                user.getId(),
                user.getNom(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().getNom(): null
        );
    }
}
