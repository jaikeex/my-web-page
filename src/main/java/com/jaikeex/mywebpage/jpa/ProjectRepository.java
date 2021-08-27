package com.jaikeex.mywebpage.jpa;

import com.jaikeex.mywebpage.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("select p from Project p where p.name = :name")
    Project findByName(
            @Param("name") String name);





}
