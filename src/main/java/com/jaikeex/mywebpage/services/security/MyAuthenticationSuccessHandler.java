package com.jaikeex.mywebpage.services.security;

import com.jaikeex.mywebpage.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@Service
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    UserRepository repository;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    public MyAuthenticationSuccessHandler(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Timestamp newLastAccessDate = new Timestamp(System.currentTimeMillis());
        String username = authentication.getName();
        repository.updateLastAccessDate(newLastAccessDate, username);
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/");
    }
}
