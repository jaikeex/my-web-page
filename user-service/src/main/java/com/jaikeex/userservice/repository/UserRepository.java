package com.jaikeex.userservice.repository;

import com.jaikeex.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.username = :username")
    User findByUsername(
            @Param("username") String username);


    @Query("select u from User u where u.email = :email")
    User findByEmail(
            @Param("email") String email);

    @Query("select u from User u where u.id = :id")
    User findUserById(
            @Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update User set lastAccessDate = :newLastAccessDate where username = :username")
    void updateLastAccessDate(
            @Param("newLastAccessDate") Timestamp newLastAccessDate,
            @Param("username") String username);

    @Modifying
    @Transactional
    @Query("update User set lastAccessDate = :newLastAccessDate where id = :id")
    void updateLastAccessDate(
            @Param("newLastAccessDate") Timestamp newLastAccessDate,
            @Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update User set password = :password where username = :username")
    void updatePassword(
            @Param("username") String username,
            @Param("password") String password);

    @Modifying
    @Transactional
    @Query("update User set password = :password where id = :id")
    void updatePassword(
            @Param("id") Integer id,
            @Param("password") String password);
}

