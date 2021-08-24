package com.jaikeex.mywebpage.jpa;

import com.jaikeex.mywebpage.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"classpath:userRepositoryDemo.sql"})
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    public void shouldFindByUsername() {
        User user = repository.findByUsername("jaikeex");
        assertNotNull(user);
    }

    @Test
    public void shouldFailWithNoSuchUser() {
        User user = repository.findByUsername("123");
        assertNull(user);
    }

    @Test
    public void shouldFindByEmail() {
        User user = repository.findByEmail("jaikeex@jaikeex.com");
        assertNotNull(user);
    }

    @Test
    public void shouldFailWithNoSuchEmail() {
        User user = repository.findByUsername("123");
        assertNull(user);
    }

    @Test
    public void shouldUpdateLastAccessDate() {
        Timestamp newAccessDate = new Timestamp(946684800000L);
        repository.updateLastAccessDate(newAccessDate, "jaikeex");
        User user = repository.findByUsername("jaikeex");
        assertEquals(newAccessDate, user.getLastAccessDate());
    }

    @Test
    public void shouldUpdatePassword() {
        String newPassword = "new_password";
        repository.updatePassword(newPassword, "jaikeex");
        User user = repository.findByUsername("jaikeex");
        assertEquals(newPassword, user.getPassword());
    }

}