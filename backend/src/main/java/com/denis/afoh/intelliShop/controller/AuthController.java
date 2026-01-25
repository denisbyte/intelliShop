package com.denis.afoh.intelliShop.controller;


import com.denis.afoh.intelliShop.dto.auth.AuthResponse;
import com.denis.afoh.intelliShop.dto.auth.LoginRequest;
import com.denis.afoh.intelliShop.dto.auth.RegisterRequest;
import com.denis.afoh.intelliShop.entity.User;
import com.denis.afoh.intelliShop.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
       User user = userService.registerUser(request.getNom(), request.getEmail(), request.getPassword());
       return ResponseEntity.ok(new AuthResponse(
               null,
               user.getId(),
               user.getEmail(),
               user.getRole().getNom()
       ));

    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        // Vérification du mot de passe via AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userService.getUserByEmail(request.getEmail());
        return ResponseEntity.ok(new AuthResponse(
                null,
                user.getId(),
                user.getEmail(),
                user.getRole().getNom()
        ));
    }
}
