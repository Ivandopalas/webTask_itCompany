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
 * Service order in request parameters. Prepare creating project page.
 * Allow connect new project with current order by holding order id in request.
 * @return page with "create project" form.
 */
public class CheckOrderCommand implements ICommand {

    private static final String EXCEPTION_SAVE_PARAM_MESSAGE =
            "Saving parameters in session problems";
    private static final String EXCEPTION_GET_ORDER_ID_MESSAGE =
            "Getting order id problems, can't prepare project creating page";


    public CheckOrderCommand(){}

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        try {
            HttpSession session = request.getSession();
            int orderId = getCurrentOrderId(request, session);
            request.setAttribute(RequestParameterName.ORDER_ID, orderId);
            UserService serviceInstance = UserService.getInstance();
            List<UserParameters> allDevelopers = serviceInstance.getAllDevelopers();
            request.setAttribute(RequestParameterName.DEVELOPER_LIST,allDevelopers);

            saveCurrentParamForLocalization(session, orderId, allDevelopers);

            PageScopeService saveUrlAction = PageScopeService.getInstance();
            saveUrlAction.saveParametersInSession(request, JspName.CREATE_PROJECT_PAGE);
            return JspName.CREATE_PROJECT_PAGE;
        }catch (ServiceException ex){
            throw new CommandException(EXCEPTION_SAVE_PARAM_MESSAGE,ex);
        }
    }
    private void saveCurrentParamForLocalization(
            HttpSession session, Object paramOrderId,Object paramDevelopers){

        PreviousRequestParameter[] params = new PreviousRequestParameter[2];
        PreviousRequestParameter currentParamOrderId = new PreviousRequestParameter();
        currentParamOrderId.setParameterName(RequestParameterName.ORDER_ID);
        currentParamOrderId.setParameter(paramOrderId);

        params[0] = currentParamOrderId;
        PreviousRequestParameter currentParamDevelopers = new PreviousRequestParameter();
        currentParamDevelopers.setParameterName(RequestParameterName.DEVELOPER_LIST);
        currentParamDevelopers.setParameter(paramDevelopers);

        params[1] = currentParamDevelopers;
        session.setAttribute(SessionParameterName.PREVIOUS_REQUEST_ATTRIBUTES,params);
    }
    private int getCurrentOrderId(
            HttpServletRequest request ,HttpSession session)throws CommandException{
        String orderIdFromRequest = request.getParameter(RequestParameterName.ORDER_ID);
        if(orderIdFromRequest == null){
            orderIdFromRequest = (String)session.getAttribute(
                    SessionParameterName.LAST_CHECKED_CLIENT_ORDER);
        }else{
            session.setAttribute(SessionParameterName.LAST_CHECKED_CLIENT_ORDER,orderIdFromRequest);
        }
        try {
            int orderId = Integer.parseInt(orderIdFromRequest);
            return orderId;
        }catch (NumberFormatException ex){
            throw new CommandException(EXCEPTION_GET_ORDER_ID_MESSAGE,ex);
        }
    }
}