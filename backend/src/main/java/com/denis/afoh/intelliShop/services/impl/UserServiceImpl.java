package com.denis.afoh.intelliShop.services.impl;


import com.denis.afoh.intelliShop.entity.Role;
import com.denis.afoh.intelliShop.entity.User;
import com.denis.afoh.intelliShop.repository.UserRepository;
import com.denis.afoh.intelliShop.services.RoleService;
import com.denis.afoh.intelliShop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    // Pour la création ou la récupération de role
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;



    @Override
    public User registerUser(String nom, String email, String rawPassword) {
        // Vérification si l'email existe déja
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("Email déja utilisé: " + email);
        }
        Role roleUser = roleService.getOrCreateRole("ROLE_USER");
        // Hachage du mot de passe
        String hashedPassword = passwordEncoder.encode(rawPassword);
        // Création de l'utilisateur
        User user = new User();
        user.setNom(nom);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRole(roleUser);

        // Sauvegarde en base de données
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Introuvable, id = " + id ));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User introuvable, email=" + email));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new RuntimeException("Suppression impossible, user introuvable id =" + id);
        }
        userRepository.deleteById(id);
    }
}
