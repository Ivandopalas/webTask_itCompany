package com.epam.it_company.command.impl.authorize;

import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.SessionParameterName;
import com.epam.it_company.controller.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Logout current user. Set user type status to unlogined.
 * @return index page.
 */
public class LogoutCommand implements ICommand {
    private final static Logger logger = LogManager.getLogger(LoginCommand.class);

    public LogoutCommand(){}

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        session.setAttribute(SessionParameterName.USER_TYPE, UserType.UNLOGINED.toString());
        String currentUserLogin = (String)session.getAttribute(SessionParameterName.USER_LOGIN);
        logger.info(currentUserLogin + " logout");
        return JspName.INDEX_PAGE;
    }
}
