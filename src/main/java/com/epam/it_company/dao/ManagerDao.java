package com.epam.it_company.dao;

import com.epam.it_company.domain.UserParameters;

import java.util.List;

/**
 * The Manager Data Access Object is the interface providing access to manager data and
 * all relations with manager.
 */
public interface ManagerDao {

    /**
     * Get all users from database which type is client.
     * @return List of beans, containing client information.
     * @throws DaoException
     */
    public List<UserParameters> getAllClient() throws DaoException;

    /**
     * Get all users from database which type is developer.
     * @return List of beans, containing developer information.
     * @throws DaoException
     */
    public List<UserParameters> getAllDevelopers() throws DaoException;

    /**
     * Create project in database. Update all dependencies with this project.
     * Modify order of this project. Set cost for this project to client.
     * Set developer list who will work on this project.
     * @param idOrder if of order on which this project is bases.
     * @param managerLogin manager who created this project.
     * @param developerList list of developers who will work on this project.
     * @param projectName name of current project.
     * @param projectCost cost of current project.
     * @return true if project added successfully and all dependencies done. false otherwise.
     * @throws DaoException
     */
    public boolean createProject(
            int idOrder, String managerLogin,
            List<UserParameters> developerList,
            String projectName, int projectCost) throws DaoException;
}