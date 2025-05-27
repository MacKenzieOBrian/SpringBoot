package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA will automatically implement this method based on the naming convention
    // It allows us to find a user by their username, which will be useful for login
    Optional<User> findByUsername(String username);

    // You can add other custom query methods here if needed, e.g.:
    // Optional<User> findByEmail(String email);
    // Boolean existsByUsername(String username);
    // Boolean existsByEmail(String email);
}
