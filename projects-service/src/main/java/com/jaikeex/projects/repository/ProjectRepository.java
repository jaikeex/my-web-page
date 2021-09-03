package com.jaikeex.projects.repository;

import com.jaikeex.projects.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("select p from Project p where p.name = :name")
    Project findByName(
            @Param("name") String name);

    @Query("select p from Project p where p.id = :projectId")
    Project findProjectById(@Param("projectId") Integer projectId);
}
