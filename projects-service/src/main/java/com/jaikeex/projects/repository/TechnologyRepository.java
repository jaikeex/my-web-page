package com.jaikeex.projects.repository;

import com.jaikeex.projects.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Integer> {

    @Query("select t from Technology t where t.name = :name")
    Technology findByName(
            @Param("name") String name);

    @Query("select t from Technology t where t.name = :technologyId")
    Technology findTechnologyById(
            @Param("technologyId") Integer technologyId);
}
