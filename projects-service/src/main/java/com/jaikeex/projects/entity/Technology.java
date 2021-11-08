package com.jaikeex.projects.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToMany(mappedBy = "technologies")
    @JsonIgnore
    Set<Project> projects = new HashSet<>();


    public void addProject(Project project) {
        if (projects.contains(project)) {
            return;
        }
        projects.add(project);
    }

    public void removeProject(Project project) {
        if (!projects.contains(project)) {
            return;
        }
        projects.remove(project);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Technology)) return false;
        Technology that = (Technology) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Technology{" +
        "name='" + name + '\'' +
        '}';
    }
}
