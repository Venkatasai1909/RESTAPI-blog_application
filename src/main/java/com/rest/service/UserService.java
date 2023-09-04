package com.rest.service;


import com.rest.model.User;

public interface UserService {
    User findByName(String name);
    void registerUser(User user);
    User findByEmail(String email);
}
