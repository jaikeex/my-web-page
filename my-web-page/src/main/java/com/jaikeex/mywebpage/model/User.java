package com.jaikeex.mywebpage.model;

import com.jaikeex.mywebpage.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User{

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

    public User (UserDto userDto) {
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

