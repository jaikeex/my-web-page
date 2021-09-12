package com.jaikeex.userservice.repository;

import com.jaikeex.userservice.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"classpath:userRepositoryDemo.sql"})
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    public void findUserByUsername_givenValidUsername_shouldReturnUser() {
        User user = repository.findUserByUsername("jaikeex");
        assertNotNull(user);
    }

    @Test
    public void findUserByUsername_givenInvalidUsername_shouldReturnNull() {
        User user = repository.findUserByUsername("123");
        assertNull(user);
    }

    @Test
    public void findUserByEmail_givenValidEmail_shouldReturnUser() {
        User user = repository.findUserByEmail("jaikeex@jaikeex.com");
        assertNotNull(user);
    }

    @Test
    public void findUserByEmail_givenInvalidEmail_shouldReturnNull() {
        User user = repository.findUserByUsername("123");
        assertNull(user);
    }

    @Test
    public void findUserById_givenValidId_shouldReturnUser() {
        User user = repository.findUserById(1);
        assertNotNull(user);
    }

    @Test
    public void findUserById_givenInvalidId_shouldReturnNull() {
        User user = repository.findUserById(111);
        assertNull(user);
    }

    @Test
    public void updateLastAccessDateByUsername_givenValidUsername_shouldUpdateLastAccessDate() {
        Timestamp newAccessDate = new Timestamp(946684800000L);
        repository.updateLastAccessDateByUsername(newAccessDate, "jaikeex");
        User user = repository.findUserByUsername("jaikeex");
        assertEquals(newAccessDate, user.getLastAccessDate());
    }

    @Test
    public void updateLastAccessDateById_givenValidId_shouldUpdateLastAccessDate() {
        Timestamp newAccessDate = new Timestamp(946684800000L);
        repository.updateLastAccessDateById(newAccessDate, 1);
        User user = repository.findUserByUsername("jaikeex");
        assertEquals(newAccessDate, user.getLastAccessDate());
    }

    @Test
    public void updatePasswordByEmail_givenValidEmail_shouldUpdatePassword() {
        String newPassword = "new_password";
        repository.updatePasswordByEmail("jaikeex@jaikeex.com", newPassword);
        User user = repository.findUserByUsername("jaikeex");
        assertEquals(newPassword, user.getPassword());
    }

    @Test
    public void updatePasswordById_givenValidEmail_shouldUpdatePassword() {
        String newPassword = "new_password";
        repository.updatePasswordById(1, newPassword);
        User user = repository.findUserByUsername("jaikeex");
        assertEquals(newPassword, user.getPassword());
    }
}