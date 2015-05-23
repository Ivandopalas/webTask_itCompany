package com.epam.it_company.controller.filter;

import com.epam.it_company.command.util.CommandName;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.RequestParameterName;
import com.epam.it_company.controller.SessionParameterName;
import com.epam.it_company.controller.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SecurityFilter implements Filter {

    private final Logger logger = LogManager.getLogger(SecurityFilter.class);

    public void destroy() {
        logger.info("Destroying security filter");
    }
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        String incomeCommand = request.getParameter(RequestParameterName.ACTION);
        if(incomeCommand == null){
            redirectPageToAccessDenied(request,response);
        }else{
            incomeCommand = incomeCommand.toUpperCase();
        }

        CommandName commandToCheck = CommandName.valueOf(incomeCommand);
        HttpSession session = ((HttpServletRequest)request).getSession();

        String userType = (String)session.getAttribute(SessionParameterName.USER_TYPE);
        UserType currentUserType;

        if(userType == null) {
            currentUserType = UserType.UNLOGINED;
        }else {
            currentUserType = UserType.valueOf(userType.toUpperCase());
        }

        if(!validateAction(commandToCheck,currentUserType)){
            redirectPageToAccessDenied(request, response);
        }

        chain.doFilter(request, response);
    }
    private boolean validateAction(CommandName commandToCheck, UserType currentUserType){

        switch (commandToCheck.getAccessType()){
            case ADMIN:
                if(currentUserType != UserType.ADMIN){
                    return false;
                }
                break;
            case CLIENT:
                if(currentUserType != UserType.CLIENT) {
                    if(currentUserType == UserType.MANAGER) {
                        if (commandToCheck == CommandName.GET_CLIENT_ORDERS) {
                            return true;
                        }
                    }
                    return false;
                }
                break;
            case MANAGER:
                if(currentUserType != UserType.MANAGER){
                    return false;
                }
                break;
            case DEVELOPER:
                if(currentUserType != UserType.DEVELOPER){
                    return false;
                }
                break;
        }
        return true;
    }
    private void redirectPageToAccessDenied (ServletRequest request,ServletResponse response)
            throws IOException,ServletException{
        String resultPage = JspName.ACCESS_DENIED_PAGE;
        RequestDispatcher dispatcher = request.getRequestDispatcher(resultPage);
        if(dispatcher != null) {
            dispatcher.forward(request, response);
        }else{
            logger.warn("Can't get dispatcher");
        }
    }
    public void init(FilterConfig fConfig) throws ServletException {
    }
}