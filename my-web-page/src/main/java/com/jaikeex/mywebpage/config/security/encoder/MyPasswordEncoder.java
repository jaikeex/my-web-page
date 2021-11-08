package com.jaikeex.mywebpage.config.security.encoder;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence password) {
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(16, 32, 1, 65536, 3);
        return encoder.encode(password);
    }

    @Override
    public boolean matches(CharSequence password, String hashString) {
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();
        return encoder.matches(password, hashString);
    }
}
