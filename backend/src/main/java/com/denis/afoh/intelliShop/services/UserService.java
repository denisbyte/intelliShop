package com.denis.afoh.intelliShop.services;
import com.denis.afoh.intelliShop.entity.User;

import java.util.List;

public interface UserService {

    User registerUser(String nom , String email, String rawPassword);

    User getUserById(Long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    void deleteUser(Long id);

}
