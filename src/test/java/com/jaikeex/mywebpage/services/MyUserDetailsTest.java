package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyUserDetailsTest {

    private final User testUser1 = new User(1, "kuba", "$argon2id$v=19$m=65536,t=3,p=1$ZzXUj9kdYQm/s7lFErt4wQ$XPemiyn0Pb2Vl9QlD37hWhxn9t1H8vadVUpuE8FGmzM",
            null, null, null, null, null, true, "ADMIN,USER");

    void getAuthorities() {
        MyUserDetails myUserDetails = new MyUserDetails(testUser1);
        Collection<SimpleGrantedAuthority> expectedResult = new ArrayList<>();
        expectedResult.add(new SimpleGrantedAuthority("ADMIN"));
        expectedResult.add(new SimpleGrantedAuthority("USER"));
        Collection<? extends GrantedAuthority> actualResult = myUserDetails.getAuthorities();
        assertEquals(expectedResult, actualResult);
    }


}