package com.jaikeex.userservice.service;

import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.service.exception.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RegistrationService {

    User registerUser(User user) throws UserAlreadyExistsException;

}
