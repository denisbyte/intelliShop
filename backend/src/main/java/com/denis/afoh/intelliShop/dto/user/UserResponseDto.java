package com.denis.afoh.intelliShop.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String nom;
    private String email;
    private String role;
}
