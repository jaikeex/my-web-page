package com.jaikeex.mywebpage.jpa;

import com.jaikeex.mywebpage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Timestamp;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.username = :username")
    User findByUsername(
            @Param("username") String username);

    @Modifying
    @Transactional
    @Query("update User set lastAccessDate = :newLastAccessDate where username = :username")
    void updateLastAccessDate(
            @Param("newLastAccessDate") Timestamp newLastAccessDate,
            @Param("username") String username);

    @Modifying
    @Transactional
    @Query("update User set password = :password where username = :username")
    void updatePassword(
            @Param("password") String password,
            @Param("username") String username);


}
