package com.jaikeex.registerservice.service;

import com.jaikeex.registerservice.jsonvo.User;
import com.jaikeex.registerservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@TestComponent
@ExtendWith(SpringExtension.class)
class RegisterServiceTest {

    @InjectMocks
    RegisterService service;

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


    @Test
    void should_register_user_with_valid_input() {
    }

    @Test
    void registerUserWithInValidUsername() {
    }

    @Test
    void registerUserWithInValidEmail() {
    }
}