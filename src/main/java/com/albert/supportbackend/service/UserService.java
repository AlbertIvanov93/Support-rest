package com.albert.supportbackend.service;

import com.albert.supportbackend.model.User;
import com.albert.supportbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllByName(String name) {
        return userRepository.findAllByName(name);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
