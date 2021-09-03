package com.jaikeex.mywebpage.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String githubLink;
    private String introduction;
    private String detailedDescription;
    private String language;
    private String snapshot;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "project_technology",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private Set<Technology> technologies = new HashSet<>();


    public Project() {
    }

    public Project(int id, String name, String githubLink, String introduction, String detailedDescription, String language, String snapshot) {
        this.id = id;
        this.name = name;
        this.githubLink = githubLink;
        this.introduction = introduction;
        this.detailedDescription = detailedDescription;
        this.language = language;
        this.snapshot = snapshot;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String description) {
        this.introduction = description;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public Set<Technology> getTechnologies() {
        return technologies;
    }

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

    public void setTechnologies(Set<Technology> technologiesUsed) {
        this.technologies = technologiesUsed;
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