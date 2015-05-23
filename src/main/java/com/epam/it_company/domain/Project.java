package com.epam.it_company.domain;

/**
 * Bean contain information about project.
 */
public class Project {
    private String projectName;
    private int idProject;
    private int timeOnProject;

    public int getTimeOnProject() {
        return timeOnProject;
    }

    public void setTimeOnProject(int timeOnProject) {
        this.timeOnProject = timeOnProject;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int projectId) {
        this.idProject = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
