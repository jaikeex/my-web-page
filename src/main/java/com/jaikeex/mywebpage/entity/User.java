package com.jaikeex.mywebpage.entity;

import com.jaikeex.mywebpage.services.security.MyPasswordEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void setPassword(String password) {
        PasswordEncoder passwordEncoder = new MyPasswordEncoder();
        this.password = passwordEncoder.encode(password);
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

