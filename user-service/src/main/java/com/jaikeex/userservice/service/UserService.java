package com.jaikeex.userservice.service;

import com.jaikeex.userservice.dto.ResetPasswordDto;
import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.repository.UserRepository;
import com.jaikeex.userservice.service.exception.InvalidResetTokenException;
import com.jaikeex.userservice.service.exception.NoSuchUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class UserService {

    PasswordEncoder encoder;
    RegistrationService registrationService;
    UserRepository repository;

    @Autowired
    public UserService(UserRepository repository,
                       RegistrationService registrationService,
                       MyPasswordEncoder encoder) {
        this.repository = repository;
        this.registrationService = registrationService;
        this.encoder = encoder;
    }


    public User findUserByEmail(String email) {
        log.info("Fetching user by email {} ", email);
        return repository.findUserByEmail(email);
    }


    public User findUserById(Integer id) {
        log.info("Fetching user by id {} ", id);
        return repository.findUserById(id);
    }


    public User findUserByUsername(String username) {
        log.info("Fetching user by username {} ", username);
        return repository.findUserByUsername(username);
    }


    public User updateLastAccessDateOfUser(String username, Timestamp newLastAccessDate) {
        log.debug("Updating last access date of user {}", username);
        repository.updateLastAccessDateByUsername(newLastAccessDate, username);
        return findUserByUsername(username);
    }


    public User updateLastAccessDateOfUser(Integer id, Timestamp newLastAccessDate) {
        log.debug("Updating last access date of user {}", id);
        repository.updateLastAccessDateById(newLastAccessDate, id);
        return findUserById(id);
    }


    public User updatePasswordOfUser(Integer id, ResetPasswordDto resetPasswordDto)
            throws InvalidResetTokenException, NoSuchUserException {
        log.debug("entering updatePasswordOfUser by id");
        User user = findUserById(id);
        checkIfUserIsNull(user);
        changePasswordInDatabase(user, resetPasswordDto);
        log.debug("exiting updatePasswordOfUser by id");
        return findUserById(id);
    }


    public User updatePasswordOfUser(String email, ResetPasswordDto resetPasswordDto)
            throws InvalidResetTokenException {
        log.debug("entering updatePasswordOfUser by email");
        User user = findUserByEmail(email);
        checkIfUserIsNull(user);
        changePasswordInDatabase(user, resetPasswordDto);
        log.debug("exiting updatePasswordOfUser by email");
        return findUserByEmail(email);
    }
    

    public void saveUserWithEncodedResetTokenToDatabase(String email, String resetToken) {
        User user = repository.findUserByEmail(email);
        checkIfUserIsNull(user);
        user.setResetPasswordToken(encoder.encode(resetToken));
        repository.save(user);
    }


    private void checkIfUserIsNull(User user) {
        if (user == null) {
            log.warn("Email provided does not exist in database");
            throw new NoSuchUserException("No user with this email exists");
        }
    }


    private void changePasswordInDatabase(User user, ResetPasswordDto resetPasswordDto) {
        String resetToken = resetPasswordDto.getResetToken();
        String newPassword = resetPasswordDto.getPassword();
        if (encoder.matches(resetToken, user.getResetPasswordToken())) {
            log.info("Changing password of user {} in database", user.getUsername());
            repository.updatePasswordById(user.getId(), newPassword);
        } else {
            log.warn("Reset password token is invalid");
            throw new InvalidResetTokenException();
        }
    }
}
