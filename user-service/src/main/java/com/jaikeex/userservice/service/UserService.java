package com.jaikeex.userservice.service;

import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserService {

    UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User findUserByName(String username) {
        return repository.findByUsername(username);
    }

    public User findUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User updatePasswordOfUser(String username, String newPassword) {
        repository.updatePassword(username, newPassword);
        return repository.findByUsername(username);
    }

    public User updatePasswordOfUser(Integer id, String newPassword) {
        repository.updatePassword(id, newPassword);
        return repository.findUserById(id);
    }

    public User updateLastAccessDateOfUser(String username) {
        Timestamp newLastAccessDate = new Timestamp(System.currentTimeMillis());
        repository.updateLastAccessDate(newLastAccessDate, username);
        return repository.findByUsername(username);
    }

    public User updateLastAccessDateOfUser(Integer id) {
        Timestamp newLastAccessDate = new Timestamp(System.currentTimeMillis());
        repository.updateLastAccessDate(newLastAccessDate, id);
        return repository.findUserById(id);
    }

    public User registerUser(User user) {
        return repository.save(user);
    }
}
