package com.jaikeex.mywebpage.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Technology {

    private int id;
    private String name;
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
    public String toString() {
        return "Technology{" +
        "name='" + name + '\'' +
        '}';
    }
}
