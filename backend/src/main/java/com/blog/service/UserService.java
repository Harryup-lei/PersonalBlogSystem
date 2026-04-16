package com.blog.service;

import com.blog.entity.User;

public interface UserService {

    User register(String username, String password, String email);

    String login(String username, String password);

    User getUserInfo(Long userId);

    User updateUserInfo(User user);
}
