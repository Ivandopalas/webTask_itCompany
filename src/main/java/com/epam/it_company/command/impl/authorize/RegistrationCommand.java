package com.epam.it_company.command.impl.authorize;

import com.epam.it_company.controller.LocalizationKeys;
import com.epam.it_company.controller.UserType;
import com.epam.it_company.domain.UserParameters;
import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.RequestParameterName;
import com.epam.it_company.service.ServiceException;
import com.epam.it_company.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * Register new user. Put data in database. Validate income parameters and
 * update page with informative message after submit.
 * @return confirm page if register done successfully otherwise wrong registration page.
 */
public class RegistrationCommand implements ICommand {
    private static final String EXCEPTION_MESSAGE =
            "New client registration problems";

    private static final String EMAIL_CHECK_REGEX =
            "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    public RegistrationCommand(){}

    @Override
    public String execute(HttpServletRequest request) throws CommandException {

        String resultPage;
        UserParameters regInfo = parseRequestAndSetAttr(request);
        if(regInfo == null){
            return JspName.REGISTRATION_PAGE;
        }
        regInfo.setUserType(UserType.CLIENT);
        try {
            boolean isSuccess = UserService.getInstance().registration(regInfo);
            if(isSuccess){
                resultPage = JspName.CONFIRM_REGISTRATION_PAGE;
            }else{
                resultPage = JspName.ERROR_REGISTRATION_PAGE;
            }
        }catch (ServiceException ex){
            throw new CommandException(EXCEPTION_MESSAGE,ex);
        }
        return resultPage;
    }
    private UserParameters parseRequestAndSetAttr(HttpServletRequest request){
        UserParameters regInfo = null;
        String name = request.getParameter(RequestParameterName.CLIENT_NAME);
        String password = request.getParameter(RequestParameterName.PASSWORD);
        String login = request.getParameter(RequestParameterName.LOGIN);
        String email = request.getParameter(RequestParameterName.EMAIL);

        if(login == null || login.length() == 0){
            request.setAttribute(
                    RequestParameterName.REGISTRATION_ERROR, LocalizationKeys.LOCAL_MES_WRONG_LOGIN);
            return regInfo;
        }
        if(password == null){
            request.setAttribute(
                    RequestParameterName.REGISTRATION_ERROR,LocalizationKeys.LOCAL_MES_WRONG_PASSWORD);
            return regInfo;
        }
        if(password.length() < 3){
            request.setAttribute(
                    RequestParameterName.REGISTRATION_ERROR,LocalizationKeys.LOCAL_MES_SHORT_PASSWORD);
            return regInfo;
        }
        if(name == null || name.length() == 0){
            request.setAttribute(
                    RequestParameterName.REGISTRATION_ERROR,LocalizationKeys.LOCAL_MES_WRONG_NAME);
            return regInfo;
        }

        if(!email.matches(EMAIL_CHECK_REGEX)){
            request.setAttribute(
                    RequestParameterName.REGISTRATION_ERROR,LocalizationKeys.LOCAL_MES_WRONG_EMAIL);
            return regInfo;
        }

        regInfo = new UserParameters();
        regInfo.setLogin(login);
        regInfo.setName(name);
        regInfo.setEmail(email);
        regInfo.setPassword(password);
        return regInfo;
    }
}
