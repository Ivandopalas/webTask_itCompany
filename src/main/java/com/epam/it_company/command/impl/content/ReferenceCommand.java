package com.epam.it_company.command.impl.content;

import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.RequestParameterName;

import javax.servlet.http.HttpServletRequest;

/**
 * Redirect to page in request scope attribute through controller.
 * @return page mentioned in request attribute
 */
public class ReferenceCommand implements ICommand {
    public ReferenceCommand(){}

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String directPage = request.getParameter(RequestParameterName.REFERENCE_PAGE);
        if (directPage == null){
            directPage = JspName.UNKNOWN_PAGE;
        }
        return directPage;
    }
}
