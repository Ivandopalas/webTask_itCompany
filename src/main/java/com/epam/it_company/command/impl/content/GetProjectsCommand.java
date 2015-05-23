package com.epam.it_company.command.impl.content;

import com.epam.it_company.domain.PreviousRequestParameter;
import com.epam.it_company.domain.Project;
import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.RequestParameterName;
import com.epam.it_company.controller.SessionParameterName;
import com.epam.it_company.service.PageScopeService;
import com.epam.it_company.service.ProjectHandling;
import com.epam.it_company.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Get all projects in database and place them in request scope
 * @return page with table that contain all projects list
 */
public class GetProjectsCommand implements ICommand {

    private static final String EXCEPTION_MESSAGE =
            "Can't get project list.";

    public GetProjectsCommand(){}

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();

        String currentLogin = (String)session.getAttribute(SessionParameterName.USER_LOGIN);
        try {
            ProjectHandling serviceInstance = ProjectHandling.getInstance();
            List<Project> allProjects = serviceInstance.getProjects(currentLogin);
            request.setAttribute(RequestParameterName.PROJECT_LIST,allProjects);
            saveParametersInSession(session,allProjects);
            PageScopeService saveUrlAction = PageScopeService.getInstance();
            saveUrlAction.saveParametersInSession(request, JspName.PROJECT_VIEW);
            return JspName.PROJECT_VIEW;
        }catch (ServiceException ex){
            throw new CommandException(EXCEPTION_MESSAGE,ex);
        }
    }
    private void saveParametersInSession(HttpSession session, Object paramProjList){
        PreviousRequestParameter[] params = new PreviousRequestParameter[1];
        PreviousRequestParameter currentParam = new PreviousRequestParameter();
        currentParam.setParameterName(RequestParameterName.PROJECT_LIST);
        currentParam.setParameter(paramProjList);
        params[0] = currentParam;
        session.setAttribute(SessionParameterName.PREVIOUS_REQUEST_ATTRIBUTES,params);
    }
}
