package com.jaikeex.mywebpage.entity;

import com.jaikeex.mywebpage.services.security.MyPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
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

    public User() {
    }

    public User(int id, String userName, String password,
                String email, String resetPasswordToken, Timestamp creationDate, Timestamp lastAccessDate,
                Timestamp updatedAt, boolean enabled, String role) {
        this.id = id;
        this.username = userName;
        this.password = password;
        this.email = email;
        this.resetPasswordToken = resetPasswordToken;
        this.creationDate = creationDate;
        this.lastAccessDate = lastAccessDate;
        this.updatedAt = updatedAt;
        this.enabled = enabled;
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        PasswordEncoder passwordEncoder = new MyPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastAccessDate(Timestamp lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public Timestamp getLastAccessDate() {
        return lastAccessDate;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
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

