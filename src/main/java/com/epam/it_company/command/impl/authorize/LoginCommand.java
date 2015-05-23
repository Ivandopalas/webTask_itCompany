package com.epam.it_company.command.impl.authorize;

import com.epam.it_company.domain.UserParameters;
import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.*;
import com.epam.it_company.service.ServiceException;
import com.epam.it_company.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Validate income user parameters and login.
 * @return page depending of current user type.
 */
public class LoginCommand implements ICommand {
    private final static Logger logger = LogManager.getLogger(LoginCommand.class);

    private static final String EXCEPTION_MESSAGE =
            "User login problems";
    public LoginCommand(){}

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        UserParameters loginInfo;
        loginInfo = parseRequest(request);
        if(loginInfo == null){
            request.setAttribute(RequestParameterName.LOGIN_ERROR, LocalizationKeys.LOGIN_WRONG);
            return JspName.INDEX_PAGE;
        }
        String resultPage;
        try {
            UserService serviceInstance = UserService.getInstance();
            UserType type = serviceInstance.getUserType(loginInfo);
            HttpSession session = request.getSession();
            resultPage = determineType(request,type);
            session.setAttribute(SessionParameterName.USER_LOGIN,loginInfo.getLogin());
            logger.info(loginInfo.getLogin()+" login");
        } catch (ServiceException ex) {
            throw new CommandException(EXCEPTION_MESSAGE,ex);
        }
        return resultPage;
    }

    private String determineType(HttpServletRequest request, UserType type){
        HttpSession session = request.getSession();
        switch (type){
            case ADMIN:
                session.setAttribute(
                        SessionParameterName.USER_TYPE,UserType.ADMIN.toString());
                return JspName.ADMIN_PAGE;
            case MANAGER:
                session.setAttribute(
                        SessionParameterName.USER_TYPE,UserType.MANAGER.toString());
                return JspName.MANAGER_PAGE;
            case DEVELOPER:
                session.setAttribute(
                        SessionParameterName.USER_TYPE,UserType.DEVELOPER.toString());
                return JspName.DEVELOPER_PAGE;
            case CLIENT:
                session.setAttribute(
                        SessionParameterName.USER_TYPE,UserType.CLIENT.toString());
                return JspName.CLIENT_PAGE;
            case UNLOGINED:
                request.setAttribute(RequestParameterName.LOGIN_ERROR,LocalizationKeys.LOGIN_INCORRECT);
                return JspName.INDEX_PAGE;
        }
        request.setAttribute(RequestParameterName.LOGIN_ERROR, LocalizationKeys.LOGIN_INCORRECT);
        return JspName.INDEX_PAGE;
    }
    private UserParameters parseRequest(HttpServletRequest request){
        String login = request.getParameter(RequestParameterName.LOGIN);
        String password = request.getParameter(RequestParameterName.PASSWORD);
        if(login == null || login.length() == 0){
            return null;
        }
        UserParameters loginInfo = new UserParameters();
        loginInfo.setLogin(login);
        loginInfo.setPassword(password);
        return loginInfo;
    }
}
