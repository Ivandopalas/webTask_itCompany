package com.epam.it_company.service;

import com.epam.it_company.controller.SessionParameterName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;


/**
 * Service class with business logic.
 * Provide cooperation of dao layer and controller layer.
 */
public class PageScopeService {
    private static final PageScopeService instance = new PageScopeService();

    private PageScopeService(){}

    public static PageScopeService getInstance(){
        return instance;
    }

    /**
     * Save current page with parameters in session from request.
     * @param request Get parameters from this request.
     * @param page current page that you want to save
     * @return true if saved successfully otherwise false;
     */
    public boolean saveParametersInSession(HttpServletRequest request, String page){
        HttpSession session = request.getSession();
        if(session == null){
            return false;
        }
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuffer params = new StringBuffer(page);
        String constructSymbol = "";
        boolean hasParameter = false;
        while (parameterNames.hasMoreElements()) {
            if(!hasParameter) {
                params.append("?");
                hasParameter = true;
            }
            String paramName = parameterNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            params.append(constructSymbol);
            params.append(paramName);
            params.append("=");
            String paramValue = paramValues[0];
            params.append(paramValue);
            constructSymbol = "&";
        }
        session.setAttribute(SessionParameterName.PREVIOUS_PARAMS, params.toString());
        return true;
    }
}
