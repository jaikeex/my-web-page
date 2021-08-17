package com.jaikeex.mywebpage.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Project {

    @Id
    private int id;
    private String name;
    private String githubLink;
    private String description;

    public Project() {
    }

    public Project(int id, String name, String githubLink, String description) {
        this.id = id;
        this.name = name;
        this.githubLink = githubLink;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGithubLink() {
        return githubLink;
    }

    public void setGithubLink(String githubLink) {
        this.githubLink = githubLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", githubLink='" + githubLink + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return getId() == project.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
