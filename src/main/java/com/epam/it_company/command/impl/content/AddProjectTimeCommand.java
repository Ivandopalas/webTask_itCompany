package com.epam.it_company.command.impl.content;

import com.epam.it_company.domain.PreviousRequestParameter;
import com.epam.it_company.domain.Project;
import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.LocalizationKeys;
import com.epam.it_company.controller.RequestParameterName;
import com.epam.it_company.controller.SessionParameterName;
import com.epam.it_company.service.ProjectHandling;
import com.epam.it_company.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Add time worked on project.
 * @return current page with some message.
 */
public class AddProjectTimeCommand implements ICommand{
    private final static Logger logger = LogManager.getLogger(AddProjectTimeCommand.class);

    private static final String EXCEPTION_MESSAGE =
            "Adding new project problems";

    public AddProjectTimeCommand(){}

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();

        String projectTimeString;
        projectTimeString = request.getParameter(RequestParameterName.PROJECT_TIME);
        String currentLogin = (String)session.getAttribute(SessionParameterName.USER_LOGIN);
        try{
            int incomeIncreaseProjectTime;
            String projectIdString;
            int projectId;
            incomeIncreaseProjectTime = getTimeFromParameter(projectTimeString);
            if(incomeIncreaseProjectTime != -1) {
                projectIdString = request.getParameter(RequestParameterName.PROJECT_ID);
                projectId = Integer.parseInt(projectIdString);
                String developerLogin = (String) session.getAttribute(SessionParameterName.USER_LOGIN);
                addTime(developerLogin,projectId,incomeIncreaseProjectTime,request);

            }else{
                request.setAttribute(RequestParameterName.PROJECT_TIME_SET_MESSAGE,
                        LocalizationKeys.PROJECT_TIMESET_WRONG);
            }
            List<Project> allProjects = ProjectHandling.getInstance().getProjects(currentLogin);
            request.setAttribute(RequestParameterName.PROJECT_LIST,allProjects);
            saveParametersInSession(session,allProjects);
        }catch (ServiceException ex){
            throw new CommandException(EXCEPTION_MESSAGE,ex);
        }
        return JspName.PROJECT_VIEW;
    }

    private void addTime(
            String developerLogin, int projectId,
            int incomeIncreaseProjectTime, HttpServletRequest request) throws ServiceException{
        ProjectHandling serviceInstance = ProjectHandling.getInstance();
        boolean isSuccess = serviceInstance.increaseTimeOnProject(
                developerLogin, projectId, incomeIncreaseProjectTime);
        if(isSuccess) {
            request.setAttribute(RequestParameterName.PROJECT_TIME_SET_MESSAGE,
                    LocalizationKeys.PROJECT_TIMESET_SUCCESS);
        }else {
            request.setAttribute(RequestParameterName.PROJECT_TIME_SET_MESSAGE,
                    LocalizationKeys.PROJECT_TIMESET_WRONG);
        }
    }
    //return -1 if can't parse income string. Otherwise parsed time.
    private int getTimeFromParameter(String incomeTimeParameter){
        if(incomeTimeParameter == null){
            return -1;
        }
        int time;
        try {
            time = Integer.parseInt(incomeTimeParameter);
            if(time <= 0){
                return -1;
            }
        }catch (NumberFormatException ex){
            logger.warn("Some info parsing problem while adding project");
            return -1;
        }
        return time;
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
