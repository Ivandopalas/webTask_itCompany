package com.epam.it_company.dao;

import com.epam.it_company.domain.Project;

import java.util.List;

/**
 * The Developer Data Access Object is the interface providing access to developer data and
 * all relations with developer.
 */
public interface DeveloperDao {

    /**
     * Get all projects which assigned to certain developer.
     * @param developerLogin Developer, who's projects you'll get.
     * @return list of domain which contain information about projects.
     * @throws DaoException
     */
    public List<Project> getProjects(String developerLogin) throws DaoException;

    /**
     * Add working time of current developer to current project.
     * @param developerLogin Login of developer
     * @param projectId id of project where current developer worked
     * @param time time that developer worked
     * @return true if time added successfully, false otherwise.
     * @throws DaoException
     */
    public boolean addWorkingTimeOnProject(
            String developerLogin,int projectId,int time) throws DaoException;
}
