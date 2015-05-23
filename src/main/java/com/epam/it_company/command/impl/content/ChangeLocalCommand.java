package com.epam.it_company.command.impl.content;

import com.epam.it_company.domain.PreviousRequestParameter;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.RequestParameterName;
import com.epam.it_company.controller.SessionParameterName;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Change local on current page.
 * @return the same page with the same parameters and attributes
 */
public class ChangeLocalCommand implements ICommand {

    public ChangeLocalCommand(){}

    @Override
    public String execute(HttpServletRequest request) {

        HttpSession session = request.getSession(true);
        Object local = request.getParameter(RequestParameterName.LOCAL);

        session.setAttribute(SessionParameterName.LOCAL,local);
        String resultPage;
        String currentPage = request.getParameter(RequestParameterName.CURRENT_PAGE);
        if(currentPage.equals(JspName.DYNAMIC_PAGE)){
            String prePage = (String)session.getAttribute(SessionParameterName.PREVIOUS_PARAMS);
            PreviousRequestParameter[] attributes = (PreviousRequestParameter[])session.getAttribute(
                    SessionParameterName.PREVIOUS_REQUEST_ATTRIBUTES);
            for(PreviousRequestParameter i : attributes){
                request.setAttribute(i.getParameterName(),i.getParameter());
            }
            if (prePage == null) {
                return  JspName.ERROR_PAGE;
            }
            resultPage = prePage;
        }else {
            resultPage = currentPage;
        }
        return resultPage;
    }
}
