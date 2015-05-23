package com.epam.it_company.command.impl.content;

import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.LocalizationKeys;
import com.epam.it_company.controller.RequestParameterName;
import com.epam.it_company.controller.SessionParameterName;
import com.epam.it_company.domain.PreviousRequestParameter;
import com.epam.it_company.domain.UserParameters;
import com.epam.it_company.service.ServiceException;
import com.epam.it_company.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class DeleteUserCommand implements ICommand {

    private static final String EXCEPTION_MESSAGE =
            "Can't delete user.";

    public DeleteUserCommand(){}

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();

        String userLogin = request.getParameter(RequestParameterName.USER_LOGIN);
        try {
            UserService serviceInstance = UserService.getInstance();
            boolean isSuccess = serviceInstance.deleteDeveloper(userLogin);
            if(isSuccess) {
                List<UserParameters> allUsers = serviceInstance.getAllUsers();
                request.setAttribute(RequestParameterName.USER_LIST,allUsers);
                saveParamsForLocalization(session,allUsers);
            }else{
                request.setAttribute(RequestParameterName.USER_DELETE_MESSAGE,
                        LocalizationKeys.DELETE_USER_ERR);
            }
            return JspName.ALL_USERS_VIEW_PAGE;
        }catch (ServiceException ex){
            throw new CommandException(EXCEPTION_MESSAGE,ex);
        }
    }

    private void saveParamsForLocalization(HttpSession session, Object paramAllUsers){
        PreviousRequestParameter[] params = new PreviousRequestParameter[1];
        PreviousRequestParameter currentParam = new PreviousRequestParameter();
        currentParam.setParameterName(RequestParameterName.USER_LIST);
        currentParam.setParameter(paramAllUsers);
        params[0] = currentParam;
        session.setAttribute(SessionParameterName.PREVIOUS_REQUEST_ATTRIBUTES,params);
    }
}
