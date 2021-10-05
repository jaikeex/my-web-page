package com.jaikeex.mywebpage.mainwebsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private int id;
    private String name;
    private String githubLink;
    private String introduction;
    private String detailedDescription;
    private String language;
    private String snapshot;
    private Set<Technology> technologies = new HashSet<>();

    public void addTechnology(Technology technology) {
        if (technologies.contains(technology)) {
            return;
        }
        technologies.add(technology);
    }

    public void removeTechnology(Technology technology) {
        if (!technologies.contains(technology)) {
            return;
        }
        technologies.remove(technology);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", githubLink='" + githubLink + '\'' +
                ", language='" + language + '\'' +
                ", technologies=" + technologies +
                '}';
    }

}
