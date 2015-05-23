package com.epam.it_company.dao.pool;

/**
 * Wraps all exceptions thrown in pool.
 * This exception may be thrown on any sql connection or init pool problems.
 */
public class ConnectionPoolException extends Exception{
    public ConnectionPoolException(Exception ex){
        super(ex);
    }
    public ConnectionPoolException(String message,Exception ex){
        super(message,ex);
    }
}
