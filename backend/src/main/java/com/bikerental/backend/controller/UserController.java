package com.bikerental.backend.controller;

import com.bikerental.backend.dto.LoginRequest;
import com.bikerental.backend.dto.RegisterRequest;
import com.bikerental.backend.dto.UserUpdateDto;
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
            
            // Check password
            if (user.getPassword().equals(loginRequest.getPassword())) {
                System.out.println("Password match!");
                
                // Check role based on login source
                if (loginRequest.isAdmin()) {
                    // Web app login (Admin only)
                    if (!user.isAdmin()) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Admins only");
                    }
                } else {
                    // Android app login (Non-admin only)
                    if (user.isAdmin()) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Users only");
                    }
                }

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
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        System.out.println("Register attempt for username: " + registerRequest.getUsername());

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(registerRequest.getPassword());
        newUser.setFirstName(registerRequest.getFirstName());
        newUser.setLastName(registerRequest.getLastName());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPhone(registerRequest.getPhone());
        
        // Use the isAdmin flag from the request, default to false if null
        boolean isAdmin = registerRequest.getIsAdmin() != null && registerRequest.getIsAdmin();
        newUser.setAdmin(isAdmin);

        User savedUser = userRepository.save(newUser);
        
        // Don't return the password
        savedUser.setPassword(null);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    if (userDetails.getUsername() != null) user.setUsername(userDetails.getUsername());
                    if (userDetails.getPassword() != null) user.setPassword(userDetails.getPassword());
                    if (userDetails.getFirstName() != null) user.setFirstName(userDetails.getFirstName());
                    if (userDetails.getLastName() != null) user.setLastName(userDetails.getLastName());
                    if (userDetails.getPhone() != null) user.setPhone(userDetails.getPhone());
                    if (userDetails.getEmail() != null) user.setEmail(userDetails.getEmail());
                    
                    // Note: isAdmin is not updated here to prevent accidental role changes via profile update.
                    // If admin status update is required, it should be handled carefully (e.g. separate endpoint or explicit check).

                    User updatedUser = userRepository.save(user);
                    updatedUser.setPassword(null); // Don't return password
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
