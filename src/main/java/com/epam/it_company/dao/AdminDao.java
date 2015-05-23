package com.epam.it_company.dao;


import com.epam.it_company.domain.UserParameters;

import java.util.List;

/**
 * The Admin Data Access Object is the interface providing access to admin data and
 * all relations with admin.
 */
public interface AdminDao {

    /**
     * Get all users from database.
     * @return List of beans, containing user information.
     * @throws DaoException
     */
    public List<UserParameters> getAllUsers() throws DaoException;
    public boolean deleteUser(String userLogin) throws DaoException;
}
