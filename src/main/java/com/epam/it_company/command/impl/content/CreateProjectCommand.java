package com.epam.it_company.command.impl.content;

import com.epam.it_company.domain.UserParameters;
import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.LocalizationKeys;
import com.epam.it_company.controller.RequestParameterName;
import com.epam.it_company.controller.SessionParameterName;
import com.epam.it_company.service.ProjectHandling;
import com.epam.it_company.service.ServiceException;
import com.epam.it_company.service.UserService;;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Creating new project according to income order id. Validate income parameters.
 * In wrong case just return current page with message.
 * @return Order confirm page if all parameters are right and new project added to database.
 * In other case return the same page with message.
 */
public class CreateProjectCommand implements ICommand {

    private static final String EXCEPTION_MESSAGE =
            "Adding new project problems";

    public CreateProjectCommand(){}

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        try {
            HttpSession session = request.getSession();
            String projectName = request.getParameter(RequestParameterName.PROJECT_NAME);
            String orderIdParam = request.getParameter(RequestParameterName.ORDER_ID);
            String projectCostString = request.getParameter(RequestParameterName.PROJECT_COST);

            List<UserParameters> developersOnProject = getDevelopersFromCheckBox(request);

            boolean isValidated = validateData(projectName,developersOnProject,projectCostString);
            if(!isValidated){
                String currentPage = getUpdatedPageWithMessage(request);
                return currentPage;
            }
            String currentManagerLogin = (String)session.getAttribute(SessionParameterName.USER_LOGIN);
            int orderId = Integer.parseInt(orderIdParam);
            int projectCost = Integer.parseInt(projectCostString);
            ProjectHandling serviceInstance = ProjectHandling.getInstance();
            boolean isSuccess = serviceInstance.addProject(orderId, currentManagerLogin,
                    developersOnProject, projectName, projectCost);
            if(!isSuccess){
                String currentPage = getUpdatedPageWithMessage(request);
                return currentPage;
            }
        }catch (ServiceException ex){
            throw new CommandException(EXCEPTION_MESSAGE,ex);
        }
        return JspName.CREATE_PROJECT_CONFIRM_PAGE;
    }
    private boolean validateData(String projectName,List<UserParameters> developersOnProject,
                                 String projectCostString){
        if(projectName == null){
            return false;
        }
        if(developersOnProject == null || developersOnProject.size() == 0){
            return false;
        }
        if(projectCostString == null){
            return false;
        }
        if(!projectCostString.matches("[0-9]+")){
            return false;
        }
        return true;
    }
    private String getUpdatedPageWithMessage(HttpServletRequest request) throws ServiceException{

        int orderId = Integer.parseInt(request.getParameter(RequestParameterName.ORDER_ID));
        UserService serviceInstance = UserService.getInstance();
        List<UserParameters> allDevelopers = serviceInstance.getAllDevelopers();
        request.setAttribute(RequestParameterName.DEVELOPER_LIST,allDevelopers);
        request.setAttribute(RequestParameterName.ORDER_ID,orderId);
        request.setAttribute(RequestParameterName.PROJECT_CREATE_MESSAGE,
                LocalizationKeys.PROJECT_PARAMETERS_WRONG);
        return JspName.CREATE_PROJECT_PAGE;
    }
    private  List<UserParameters> getDevelopersFromCheckBox(HttpServletRequest request){
        List<UserParameters> markedDevelopers = new ArrayList<>();
        String allDevNumString = request.getParameter(RequestParameterName.DEVELOPER_COUNT);
        int allDevNum = Integer.parseInt(allDevNumString);
        for(Integer i = 0 ; i <= allDevNum; i++) {
            String currentDeveloperLogin = request.getParameter(
                    RequestParameterName.DEVELOPER_NUM.concat(i.toString()));
            if (currentDeveloperLogin != null) {
                UserParameters currentDeveloper = new UserParameters();
                currentDeveloper.setLogin(currentDeveloperLogin);
                markedDevelopers.add(currentDeveloper);
            }
        }
        return markedDevelopers;
    }
}
