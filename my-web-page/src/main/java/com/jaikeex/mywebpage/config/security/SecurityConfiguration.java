package com.jaikeex.mywebpage.config.security;

import com.jaikeex.mywebpage.config.security.encoder.MyPasswordEncoder;
import com.jaikeex.mywebpage.config.security.userdetails.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    UserDetailsService userDetailsService;
    AuthenticationSuccessHandler successHandler;

    @Autowired
    public SecurityConfiguration(MyUserDetailsService userDetailsService, MyAuthenticationSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        setAdminPageRules(http);
        setUserInfoRules(http);
        setOptionsRequestsRules(http);
        setTrackerRules(http);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new MyPasswordEncoder();
    }

    private void setOptionsRequestsRules(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
    }

    private void setTrackerRules(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/tracker/update")
                    .hasRole("ADMIN")
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .successHandler(successHandler);

        http.authorizeRequests()
                    .antMatchers("/tracker/create")
                    .hasAnyRole("USER", "ADMIN")
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .successHandler(successHandler);
    }

    private void setUserInfoRules(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/user/auth/**")
                    .hasAnyRole("USER", "ADMIN")
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .successHandler(successHandler);
    }

    private void setAdminPageRules(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/admin/**")
                    .hasRole("ADMIN")
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .successHandler(successHandler);
    }
}
