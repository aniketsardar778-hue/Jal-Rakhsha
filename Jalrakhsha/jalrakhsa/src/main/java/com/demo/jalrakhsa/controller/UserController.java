package com.demo.jalrakhsa.controller;


import com.demo.jalrakhsa.entity.User;
import com.demo.jalrakhsa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private  UserService userService;


    // ==========================================
    // REGISTER
    // ==========================================

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestBody User user) {

        User savedUser =
                userService.registerUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }


    // ==========================================
    // LOGIN
    // ==========================================

    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestParam String email,
            @RequestParam String password) {

        User user =
                userService.login(
                        email,
                        password
                );

        return ResponseEntity.ok(user);
    }


    // ==========================================
    // GET USER
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @PathVariable Long id) {

        User user =
                userService.getUserById(id);

        return ResponseEntity.ok(user);
    }


    // ==========================================
    // GET ALL USERS
    // ==========================================

    @GetMapping("/all")
    public ResponseEntity<List<User>>
    getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }


    // ==========================================
    // DELETE USER
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }
}
