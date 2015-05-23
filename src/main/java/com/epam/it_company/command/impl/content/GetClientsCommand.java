package com.epam.it_company.command.impl.content;

import com.epam.it_company.domain.PreviousRequestParameter;
import com.epam.it_company.domain.UserParameters;
import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.RequestParameterName;
import com.epam.it_company.controller.SessionParameterName;
import com.epam.it_company.service.PageScopeService;
import com.epam.it_company.service.ServiceException;
import com.epam.it_company.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Get all clients in database and place them in request scope
 * @return page with table that contain all clients list
 */
public class GetClientsCommand implements ICommand {

    private static final String EXCEPTION_MESSAGE =
            "Can't get client list.";

    public GetClientsCommand(){}

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        try {
            HttpSession session = request.getSession();

            UserService serviceInstance = UserService.getInstance();
            List<UserParameters> allClients = serviceInstance.getAllClients();

            request.setAttribute(RequestParameterName.CLIENT_LIST,allClients);
            saveParamsForLocalization(session,allClients);
            PageScopeService saveUrlAction = PageScopeService.getInstance();
            saveUrlAction.saveParametersInSession(request, JspName.ALL_CLIENT_VIEW_PAGE);

            return JspName.ALL_CLIENT_VIEW_PAGE;
        }catch (ServiceException ex){
            throw new CommandException(EXCEPTION_MESSAGE,ex);
        }
    }
    private void saveParamsForLocalization(HttpSession session, Object paramAllClients){
        PreviousRequestParameter[] params = new PreviousRequestParameter[1];
        PreviousRequestParameter currentParam = new PreviousRequestParameter();
        currentParam.setParameterName(RequestParameterName.CLIENT_LIST);
        currentParam.setParameter(paramAllClients);
        params[0] = currentParam;
        session.setAttribute(SessionParameterName.PREVIOUS_REQUEST_ATTRIBUTES,params);
    }
}
