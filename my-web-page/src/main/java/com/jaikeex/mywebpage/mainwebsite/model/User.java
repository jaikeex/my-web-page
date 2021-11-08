package com.jaikeex.mywebpage.mainwebsite.model;

import com.jaikeex.mywebpage.mainwebsite.dto.UserRegistrationFormDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class User{
    //TODO: This needs more functionality.

    private int id;
    private String username;
    private String password;
    private String email;
    private String resetPasswordToken;
    private Timestamp creationDate;
    private Timestamp lastAccessDate;
    private Timestamp updatedAt;
    private boolean enabled;
    private String role;

    public User (UserRegistrationFormDto userDto) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        userDto.encodePassword();
        this.setUsername(userDto.getUsername());
        this.setPassword(userDto.getPassword());
        this.setEmail(userDto.getEmail());
        this.setEnabled(true);
        this.setCreationDate(now);
        this.setLastAccessDate(now);
        this.setUpdatedAt(now);
        this.setRole("ROLE_USER");
        log.debug("Initialized User from UserDto [user={}]", this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "User{" +
                "NAME='" + username + '\'' +
                ", ROLE='" + role + '\'' +
                '}';
    }
}

