package com.jaikeex.mywebpage.config.security;

import com.jaikeex.mywebpage.config.security.userdetails.MyUserDetails;
import com.jaikeex.mywebpage.mainwebsite.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MyUserDetailsTest {

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
                "ADMIN,USER");

    @Test
    void getAuthorities() {
        MyUserDetails myUserDetails = new MyUserDetails(testUser1);
        Collection<SimpleGrantedAuthority> expectedResult = new ArrayList<>();
        expectedResult.add(new SimpleGrantedAuthority("ADMIN"));
        expectedResult.add(new SimpleGrantedAuthority("USER"));
        Collection<? extends GrantedAuthority> actualResult = myUserDetails.getAuthorities();
        assertEquals(expectedResult, actualResult);
    }
}