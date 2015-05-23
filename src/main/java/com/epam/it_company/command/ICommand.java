package com.epam.it_company.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Command interface.
 */
public interface ICommand {
    /**
     * Execute current command. Command depend on it type.
     * @param request Request that came to server.
     * @return Whole page name, possibly with parameters.
     * @throws CommandException
     */
    public String execute(HttpServletRequest request) throws CommandException;
}
