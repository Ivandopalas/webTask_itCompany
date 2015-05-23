package com.epam.it_company.command.impl.content;

import com.epam.it_company.domain.Order;
import com.epam.it_company.domain.PreviousRequestParameter;
import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.RequestParameterName;
import com.epam.it_company.controller.SessionParameterName;
import com.epam.it_company.controller.UserType;
import com.epam.it_company.service.OrderHandling;
import com.epam.it_company.service.PageScopeService;
import com.epam.it_company.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Get all orders that current client made.
 * Put data in request scope.
 * @return page with table that contain orders
 */
public class GetClientOrdersCommand implements ICommand{

    private static final String EXCEPTION_MESSAGE =
            "Can't get orders, some service problems";

    public GetClientOrdersCommand(){}

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        String currentClientLogin = getCurrentClientLogin(request,session);
        try {
            OrderHandling serviceInstance = OrderHandling.getInstance();
            List<Order> allUserOrders = serviceInstance.getAllOrders(currentClientLogin);
            request.setAttribute(RequestParameterName.ORDER_LIST, allUserOrders);
            saveParamsForLocalization(session, allUserOrders);
        }catch (ServiceException ex){
            throw new CommandException(EXCEPTION_MESSAGE,ex);
        }
        String currentUserString = (String)session.getAttribute(SessionParameterName.USER_TYPE);
        UserType userType = UserType.valueOf(currentUserString);

        PageScopeService saveUrlAction = PageScopeService.getInstance();

        if(userType == UserType.CLIENT) {
            saveUrlAction.saveParametersInSession(request, JspName.SHOW_CLIENT_ORDER_PAGE);
            return JspName.SHOW_CLIENT_ORDER_PAGE;
        }else{
            saveUrlAction.saveParametersInSession(request, JspName.MANAGER_SHOW_CLIENT_ORDER_PAGE);
            return JspName.MANAGER_SHOW_CLIENT_ORDER_PAGE;
        }
    }
    private void saveParamsForLocalization(HttpSession session, Object paramAllOrders){
        PreviousRequestParameter[] params = new PreviousRequestParameter[1];
        PreviousRequestParameter currentParam = new PreviousRequestParameter();
        currentParam.setParameterName(RequestParameterName.ORDER_LIST);
        currentParam.setParameter(paramAllOrders);
        params[0] = currentParam;
        session.setAttribute(SessionParameterName.PREVIOUS_REQUEST_ATTRIBUTES,params);
    }
    private String getCurrentClientLogin(HttpServletRequest request ,HttpSession session){
        String currentClientLogin = request.getParameter(RequestParameterName.CLIENT_LOGIN);
        if(currentClientLogin == null){
            currentClientLogin = (String)session.getAttribute(
                    SessionParameterName.LAST_CHECKED_CLIENT_ORDER);
        }else{
            session.setAttribute(SessionParameterName.LAST_CHECKED_CLIENT_ORDER,currentClientLogin);
        }
        return currentClientLogin;
    }
}