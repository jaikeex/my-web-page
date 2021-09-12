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
    User findUserByUsername(
            @Param("username") String username);


    @Query("select u from User u where u.email = :email")
    User findUserByEmail(
            @Param("email") String email);

    @Query("select u from User u where u.id = :id")
    User findUserById(
            @Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update User set lastAccessDate = :newLastAccessDate where username = :username")
    void updateLastAccessDateByUsername(
            @Param("newLastAccessDate") Timestamp newLastAccessDate,
            @Param("username") String username);

    @Modifying
    @Transactional
    @Query("update User set lastAccessDate = :newLastAccessDate where id = :id")
    void updateLastAccessDateById(
            @Param("newLastAccessDate") Timestamp newLastAccessDate,
            @Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update User set password = :password where email = :email")
    void updatePasswordByEmail(
            @Param("email") String email,
            @Param("password") String password);

    @Modifying
    @Transactional
    @Query("update User set password = :password where id = :id")
    void updatePasswordById(
            @Param("id") Integer id,
            @Param("password") String password);
}

