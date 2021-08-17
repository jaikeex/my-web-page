package com.jaikeex.mywebpage.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private String password;
    private Timestamp creationDate;
    private Timestamp lastAccessDate;
    private Timestamp updatedAt;
    private boolean enabled;
    private String role;

    public User() {
    }

    public User(int id, String userName, String password,
                Timestamp creationDate, Timestamp lastAccessDate,
                Timestamp updatedAt, boolean enabled, String role) {
        this.id = id;
        this.username = userName;
        this.password = password;
        this.creationDate = creationDate;
        this.lastAccessDate = lastAccessDate;
        this.updatedAt = updatedAt;
        this.enabled = enabled;
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

