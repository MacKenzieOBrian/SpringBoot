package com.example.demo.service;

import com.example.demo.model.Post;

public interface PostService {
    Post createPost(Long userId, String title, String content);
}
