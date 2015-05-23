package com.epam.it_company.dao;

/**
 * Wraps all exceptions thrown in dao layer.
 * This exception may be thrown on any sql connection or pool problems.
 */
public class DaoException extends Exception {
    public DaoException(Exception ex){
        super(ex);
    }
    public DaoException(String message,Exception ex){
        super(message,ex);
    }
    public DaoException(String message){ super(message);}
}
