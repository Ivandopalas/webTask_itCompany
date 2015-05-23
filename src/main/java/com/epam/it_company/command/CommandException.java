package com.epam.it_company.command;

/**
 * This exception may be thrown on any method in command layer
 * or if exception caught from layer below.
 */
public class CommandException extends Exception{
    public CommandException(Exception ex){
        super(ex);
    }
    public CommandException(String message, Exception ex){
        super(message,ex);
    }
    public CommandException(String message){super(message);}
}

