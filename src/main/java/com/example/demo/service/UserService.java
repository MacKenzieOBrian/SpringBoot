package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // For managing transactions

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true) // Good practice for read operations
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional // Default transactionality (read/write)
    public User saveUser(User user) {
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id)); // Basic exception handling

        existingUser.setUsername(userDetails.getUsername());
        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        // Only update password if a new one is provided and not blank
        if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        // Add logic for updating other fields like roles if necessary

        return userRepository.save(existingUser);
    }


    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id); // Basic exception handling
        }
        userRepository.deleteById(id);
    }
}
