package com.jaikeex.mywebpage.jpa;

import com.jaikeex.mywebpage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.username = :username")
    User findByUsername(
            @Param("username") String username
    );

}
