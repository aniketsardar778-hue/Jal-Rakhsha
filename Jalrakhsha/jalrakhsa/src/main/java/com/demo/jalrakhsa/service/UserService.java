package com.demo.jalrakhsa.service;


import com.demo.jalrakhsa.entity.User;
import com.demo.jalrakhsa.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

   @Autowired
   private UserRepo userRepo;



    // ==========================================
    // REGISTER USER
    // ==========================================

    public User registerUser(User user) {

        // Check duplicate email

        if (userRepo.existsByEmail(
                user.getEmail())) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        // For DEMO only.
        // In production use BCrypt password hashing.

        return userRepo.save(user);
    }


    // ==========================================
    // LOGIN
    // ==========================================

    public User login(
            String email,
            String password) {

        User user =
                userRepo
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Invalid email or password"
                                )
                        );

        if (!user.getPassword()
                .equals(password)) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        return user;
    }


    // ==========================================
    // GET USER BY ID
    // ==========================================

    public User getUserById(Long id) {

        return userRepo
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );
    }


    // ==========================================
    // GET ALL USERS
    // ==========================================

    public List<User> getAllUsers() {

        return userRepo.findAll();
    }








    // ==========================================
    // DELETE USER
    // ==========================================

    public void deleteUser(Long id) {

        if (!userRepo.existsById(id)) {

            throw new RuntimeException(
                    "User not found"
            );
        }

        userRepo.deleteById(id);
    }
}