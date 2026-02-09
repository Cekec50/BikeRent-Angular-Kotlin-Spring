package com.bikerental.backend.controller;

import com.bikerental.backend.dto.LoginRequest;
import com.bikerental.backend.model.User;
import com.bikerental.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // Adjust if your Angular app runs on a different port
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login attempt for username: " + loginRequest.getUsername());
        
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("User found: " + user.getUsername());
            System.out.println("Stored password: " + user.getPassword());
            System.out.println("Provided password: " + loginRequest.getPassword());
            
            // In a real application, you should use a password encoder (e.g., BCrypt)
            // and not store plain text passwords.
            if (user.getPassword().equals(loginRequest.getPassword())) {
                System.out.println("Password match!");
                // Return user details or a JWT token here.
                // For simplicity, we are returning the user object, but be careful not to expose sensitive data like password.
                user.setPassword(null); // Don't send the password back
                return ResponseEntity.ok(user);
            } else {
                System.out.println("Password mismatch.");
            }
        } else {
            System.out.println("User not found.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        System.out.println("Register attempt for username: " + newUser.getUsername());

        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Ensure ID is null so it gets auto-generated
        newUser.setId(null);
        // Default to non-admin if not specified (though frontend sends false)
        // newUser.setAdmin(false); 

        User savedUser = userRepository.save(newUser);
        
        // Don't return the password
        savedUser.setPassword(null);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}
