package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.entity.User;
import com.jaikeex.mywebpage.jpa.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestComponent
@ExtendWith(SpringExtension.class)
class UserAccountManagementServiceTest {

    private final User testUser1 = new User(
            1,
            "testuserfordbaccess",
            "$argon2id$v=19$m=65536,t=3,p=1$peMkKGWTfioAQols1mso3A$dG2V75p0v6onSrFT9kOtMqhwmqOCsySt6la1QYtH2Jc",
            "testuserfordbaccess@testuserfordbaccess.com",
            "$argon2id$v=19$m=65536,t=3,p=1$ZRzpyukFgnnO1m6bkadOGA$2RxS99w4CBZVGQ/vXy8TMbr7VvXcifba2FCJePEyA/4",
            null,
            null,
            null,
            true,
            "USER");

    private final UserDto userDto = new UserDto(
            "testuserfordbaccess@testuserfordbaccess.com",
            "testuserfordbaccess",
            "testuserfordbaccess",
            "testuserfordbaccess");


    @Mock
    UserRepository repository;

    @InjectMocks
    UserAccountManagementService service;

    @Test
    void registerUserWithValidInput() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Model model = mock(Model.class);
        when(repository.save(any(User.class))).thenReturn(testUser1);
        when(repository.findByUsername(anyString())).thenReturn(null);
        when(repository.findByEmail(anyString())).thenReturn(null);
        assertNotNull(service.registerUser(userDto, request, model));
    }

    @Test
    void registerUserWithInValidUsername() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Model model = mock(Model.class);
        when(repository.save(any(User.class))).thenReturn(testUser1);
        when(repository.findByUsername(anyString())).thenReturn(testUser1);
        when(repository.findByEmail(anyString())).thenReturn(null);
        assertNull(service.registerUser(userDto, request, model));
    }

    @Test
    void registerUserWithInValidEmail() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Model model = mock(Model.class);
        when(repository.save(any(User.class))).thenReturn(testUser1);
        when(repository.findByUsername(anyString())).thenReturn(null);
        when(repository.findByEmail(anyString())).thenReturn(testUser1);
        assertNull(service.registerUser(userDto, request, model));
    }



}