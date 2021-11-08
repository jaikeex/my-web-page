package com.jaikeex.mywebpage.config.security.userdetails;

import com.jaikeex.mywebpage.mainwebsite.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MyUserDetails implements UserDetails {

    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Stream<String> authorities = Arrays.stream(user.getRole().split(","));
        return authorities.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public Timestamp getCreationDate() {
        return user.getCreationDate();
    }

    public String getTimeSinceLastLogin() {
        long timeSinceLastLoginInMilliseconds = System.currentTimeMillis() - user.getLastAccessDate().getTime();
        int daysElapsed = ((int)timeSinceLastLoginInMilliseconds / 86400000);
        DateFormat formatter = new SimpleDateFormat(" 'days' HH 'hours' mm 'minutes'");
        return String.valueOf(daysElapsed).concat(formatter.format(timeSinceLastLoginInMilliseconds-3600000));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyUserDetails)) return false;
        MyUserDetails that = (MyUserDetails) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
