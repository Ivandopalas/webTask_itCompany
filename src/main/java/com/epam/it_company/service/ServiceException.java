package com.epam.it_company.service;

/**
 * This exception may be thrown on any method in service layer.
 */
public class ServiceException extends Exception {
    public ServiceException(Exception ex){
        super(ex);
    }
    public ServiceException(String message){
        super(message);
    }
    public ServiceException(String message, Exception ex){
        super(message,ex);
    }
}
