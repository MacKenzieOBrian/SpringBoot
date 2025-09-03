
/*
 * Post.java
 * Purpose: Represents a user-created post in the application.
 * Why needed: Enables users to create content, which can be monetized or linked to transactions.
 * Reference: https://www.baeldung.com/jpa-entities
 */
package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
}
