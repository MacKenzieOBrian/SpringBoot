package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // For validating request body
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users") // Base path for all user-related endpoints
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/users - Create a new user
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Handle duplicate username or email
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or email already exists.");
        } catch (Exception e) {
            // Handle other potential errors during user creation
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user registration.");
        }
    }

    // GET /api/users - Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users); // OK (200)
    }

    // GET /api/users/{id} - Get a single user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok) // If found, return OK (200) with user
                .orElse(ResponseEntity.notFound().build()); // If not found, return Not Found (404)
    }

    // PUT /api/users/{id} - Update an existing user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser); // OK (200)
        } catch (RuntimeException e) {
    // Basic error handling if user not found during update
    return ResponseEntity.notFound().build(); // Not Found (404)
}
    }

    // DELETE /api/users/{id} - Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // No Content (204) indicates success
        } catch (RuntimeException e) {
            // Basic error handling if user not found during delete
            return ResponseEntity.notFound().build(); // Not Found (404)
        }
    }
}
