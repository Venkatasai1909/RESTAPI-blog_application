package com.rest.service.implementations;

import com.rest.model.User;
import com.rest.repository.UserRepository;
import com.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User findByName(String name) {

        return userRepository.findByName(name);
    }


    @Override
    public void registerUser(User user) {
        String bcryptPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(bcryptPassword);
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {

        return userRepository.findByEmail(email);
    }

}
