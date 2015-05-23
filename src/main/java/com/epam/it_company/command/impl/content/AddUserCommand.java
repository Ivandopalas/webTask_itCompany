package com.epam.it_company.command.impl.content;

import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.LocalizationKeys;
import com.epam.it_company.controller.RequestParameterName;
import com.epam.it_company.controller.UserType;
import com.epam.it_company.domain.UserParameters;
import com.epam.it_company.service.ServiceException;
import com.epam.it_company.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class AddUserCommand implements ICommand {

    private static final String EXCEPTION_MESSAGE =
            "Adding user problems";

    private static final String EMAIL_CHECK_REGEX =
            "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    public AddUserCommand(){}

    @Override
    public String execute(HttpServletRequest request) throws CommandException {

        String resultPage;
        UserParameters regInfo = parseRequestAndSetAttr(request);
        if(regInfo == null){
            return JspName.ADD_USER_PAGE;
        }
        try {
            boolean isSuccess = UserService.getInstance().registration(regInfo);
            if(isSuccess){
                request.setAttribute(
                        RequestParameterName.ADD_USER_MESSAGE, LocalizationKeys.ADD_USER_SUCCESS);
            }else{
                request.setAttribute(
                        RequestParameterName.ADD_USER_MESSAGE, LocalizationKeys.ADD_USER_ERR);
            }
            resultPage = JspName.ADD_USER_PAGE;
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
        String userType = request.getParameter(RequestParameterName.USER_TYPE);
        UserType type;
        if(userType == null){
            request.setAttribute(
                    RequestParameterName.ADD_USER_MESSAGE, LocalizationKeys.ADD_USER_ERR);
            return regInfo;
        }
        try{
            type = UserType.valueOf(userType);
        }catch (IllegalArgumentException ex){
            request.setAttribute(
                    RequestParameterName.ADD_USER_MESSAGE, LocalizationKeys.ADD_USER_ERR);
            return regInfo;
        }
        if(login == null || login.length() == 0){
            request.setAttribute(
                    RequestParameterName.ADD_USER_MESSAGE, LocalizationKeys.LOCAL_MES_WRONG_LOGIN);
            return regInfo;
        }
        if(password == null){
            request.setAttribute(
                    RequestParameterName.ADD_USER_MESSAGE,LocalizationKeys.LOCAL_MES_WRONG_PASSWORD);
            return regInfo;
        }
        if(name == null || name.length() == 0){
            request.setAttribute(
                    RequestParameterName.ADD_USER_MESSAGE,LocalizationKeys.LOCAL_MES_WRONG_NAME);
            return regInfo;
        }
        if(!email.matches(EMAIL_CHECK_REGEX)){
            request.setAttribute(
                    RequestParameterName.ADD_USER_MESSAGE,LocalizationKeys.LOCAL_MES_WRONG_EMAIL);
            return regInfo;
        }

        regInfo = new UserParameters();
        regInfo.setLogin(login);
        regInfo.setName(name);
        regInfo.setEmail(email);
        regInfo.setPassword(password);
        regInfo.setUserType(type);
        return regInfo;
    }
}
