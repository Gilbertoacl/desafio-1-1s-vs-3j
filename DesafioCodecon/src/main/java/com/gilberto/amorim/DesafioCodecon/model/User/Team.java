package com.gilberto.amorim.DesafioCodecon.model.User;

import java.util.List;

public class Team {
    private String name;
    private Boolean leader;
    private List<Projects> projects;

    public Team () {}

    public Team(String name, Boolean leader, List<Projects> projects) {
        this.name = name;
        this.leader = leader;
        this.projects = projects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLeader() {
        return leader;
    }

    public void setLeader(Boolean leader) {
        this.leader = leader;
    }

    public List<Projects> getProjects() {
        return projects;
    }

    public void setProjects(List<Projects> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", leader=" + leader +
                ", projects=" + projects +
                '}';
    }
}
