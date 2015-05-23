package com.epam.it_company.dao;

import com.epam.it_company.domain.UserParameters;

/**
 * The Developer Data Access Object is the interface providing access to
 * login and password data if database.
 */
public interface UserDao {
    /**
     * Validate login and password in database.
     * @param login User login.
     * @param password User password
     * @return User type if login and password correct.
     * Unlogined user type if login or password wrong
     * @throws DaoException
     */
    public String login(String login,String password) throws DaoException;
    /**
     * Add new client to database.
     * @param regInfo domain contain all user information
     * @return true is user added successfully False otherwise.
     * @throws DaoException
     */
    public boolean registerClient(UserParameters regInfo) throws DaoException;
}
