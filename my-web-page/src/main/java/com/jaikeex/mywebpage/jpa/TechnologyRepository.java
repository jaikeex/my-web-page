package com.jaikeex.mywebpage.jpa;

import com.jaikeex.mywebpage.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TechnologyRepository extends JpaRepository<Technology, Integer> {

    @Query("select t from Technology t where t.name = :name")
    Technology findByName(
            @Param("name") String name);







}
