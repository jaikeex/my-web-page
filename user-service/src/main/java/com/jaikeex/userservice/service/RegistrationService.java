package com.jaikeex.userservice.service;

import com.jaikeex.userservice.dto.UserDto;
import com.jaikeex.userservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RegistrationService {

    User registerUser(User user) throws Exception;

}
