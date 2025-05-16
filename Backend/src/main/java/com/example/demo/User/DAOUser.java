package com.example.demo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DAOUser {

    @Autowired
    private UserRepository userRepository;

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public User findUsername(String username) {
        List<User> users = userRepository.findByUsername(username);
        return users.isEmpty() ? null : users.get(0);
    }

    public User findUsernameNameEmail(String username, String name, String email) {
        return userRepository.findByUsernameAndNameAndEmail(username, name, email).orElse(null);
    }

    public User findUsernameNamePhone(String username, String name, String phone) {
        return userRepository.findByUsernameAndNameAndPhone(username, name, phone).orElse(null);
    }

    public User findNameEmail(String name, String email) {
        return userRepository.findByNameAndEmail(name, email).orElse(null);
    }

    public User findNamePhone(String name, String phone) {
        return userRepository.findByNameAndPhone(name, phone).orElse(null);
    }

    public void Insert(User user) {
        user.setId(null);
        userRepository.save(user);
    }

    public void Modify(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void Delete(User user) {
        userRepository.deleteById(user.getId());
    }
}
