package com.jaikeex.userservice.service;

import com.jaikeex.userservice.dto.UserDto;
import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class RegistrationServiceImpl implements RegistrationService{

    UserRepository repository;

    @Autowired
    public RegistrationServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User registerUser(User user) throws Exception {
        if (canBeRegistered(user)) {
            repository.save(user);
            return user;
        }
        else {
            return null;
        }
    }

    private boolean canBeRegistered(User user) throws Exception{
        return hasOriginalUsername(user) && hasOriginalEmail(user);
    }

    private boolean hasOriginalEmail(User user) throws Exception {
        User dbResponse = repository.findByEmail(user.getEmail());
        if (dbResponse != null) {
            throw new Exception("User with this email already exists");
        } else {
            return true;
        }
    }

    private boolean hasOriginalUsername(User user) throws Exception{
        User dbResponse = repository.findByUsername(user.getUsername());
        if (dbResponse != null) {
            throw new Exception("User with this name already exists");
        } else {
            return true;
        }
    }

}
