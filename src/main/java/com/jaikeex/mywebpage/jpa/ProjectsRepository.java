package com.jaikeex.mywebpage.jpa;

import com.jaikeex.mywebpage.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectsRepository extends JpaRepository<Project, Integer> {
}
