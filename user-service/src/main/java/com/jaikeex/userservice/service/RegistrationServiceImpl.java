package com.jaikeex.userservice.service;

import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.repository.UserRepository;
import com.jaikeex.userservice.service.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService{

    UserRepository repository;

    @Autowired
    public RegistrationServiceImpl(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public User registerUser(User user) throws UserAlreadyExistsException {
        if (canBeRegistered(user)) {
            log.info("Registering new user: {}", user.getUsername());
            repository.save(user);
        }
        return user;
    }


    private boolean canBeRegistered(User user) throws UserAlreadyExistsException{
        return hasOriginalUsername(user) && hasOriginalEmail(user);
    }


    private boolean hasOriginalEmail(User user) throws UserAlreadyExistsException {
        User dbResponse = repository.findUserByEmail(user.getEmail());
        if (dbResponse != null) {
            throw new UserAlreadyExistsException("User with this email already exists");
        } else {
            return true;
        }
    }


    private boolean hasOriginalUsername(User user) throws UserAlreadyExistsException{
        User dbResponse = repository.findUserByUsername(user.getUsername());
        if (dbResponse != null) {
            throw new UserAlreadyExistsException("User with this name already exists");
        } else {
            return true;
        }
    }
}
