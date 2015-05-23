package com.epam.it_company.command.impl.content;

import com.epam.it_company.domain.Job;
import com.epam.it_company.domain.Order;
import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.controller.JspName;
import com.epam.it_company.controller.LocalizationKeys;
import com.epam.it_company.controller.RequestParameterName;
import com.epam.it_company.controller.SessionParameterName;
import com.epam.it_company.service.OrderHandling;
import com.epam.it_company.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Create new order according income parameters.
 * @return current page with information message add or not new order in database.
 */
public class CreateOrderCommand implements ICommand{
    private final static Logger logger = LogManager.getLogger(CreateOrderCommand.class);

    private static final String EXCEPTION_MESSAGE =
            "Adding new order problems";

    public CreateOrderCommand(){}
    @Override
    public String execute(HttpServletRequest request) throws CommandException {

        String orderName = request.getParameter(RequestParameterName.ORDER_NAME);
        if(orderName == null || orderName.length() == 0) {
            request.setAttribute(RequestParameterName.ORDER_ADD_MESSAGE,
                    LocalizationKeys.ORDER_PARAMETERS_WRONG);
            return JspName.ADD_ORDER_PAGE;
        }

        ArrayList<JSONObject> jobsLines = new ArrayList<>();
        int jobCounter = 0;
        String job = request.getParameter(RequestParameterName.JOBS+jobCounter);
        while(job != null ){
            JSONObject jsonJob = new JSONObject(job);
            jobsLines.add(jsonJob);
            jobCounter++;
            job = request.getParameter(RequestParameterName.JOBS+jobCounter);
        }
        List<Job> allJobs = parseJobs(jobsLines);
        if(allJobs == null || allJobs.size() == 0){
            request.setAttribute(RequestParameterName.ORDER_ADD_MESSAGE,
                    LocalizationKeys.ORDER_PARAMETERS_WRONG);
            return JspName.ADD_ORDER_PAGE;
        }
        Order incomeOrder = new Order();
        incomeOrder.setName(orderName);
        incomeOrder.setJobList(allJobs);

        addOrder(request,incomeOrder);

        return JspName.ADD_ORDER_PAGE;
    }

    private void addOrder(HttpServletRequest request, Order incomeOrder) throws CommandException{
        HttpSession session = request.getSession();
        try {
            boolean isSuccess;
            String currentUser = (String)session.getAttribute(SessionParameterName.USER_LOGIN);
            OrderHandling serviceInstance = OrderHandling.getInstance();
            isSuccess = serviceInstance.addOrder(incomeOrder, currentUser);
            if(isSuccess){
                request.setAttribute(RequestParameterName.ORDER_ADD_MESSAGE,
                        LocalizationKeys.ORDER_CONFIRM);
            }else {
                request.setAttribute(RequestParameterName.ORDER_ADD_MESSAGE,
                        LocalizationKeys.ORDER_PARAMETERS_WRONG);
            }
        }catch (ServiceException ex){
            throw new CommandException(EXCEPTION_MESSAGE,ex);
        }
    }

    private List<Job> parseJobs(ArrayList<JSONObject> jobsLines){
        List<Job> allJobs = new ArrayList<>();
        try {
            for(JSONObject jobJson:jobsLines){
                Job currentJob = new Job();
                Iterator iter = jobJson.keys();

                String devQualification = jobJson.getString((String)iter.next());
                String devNumString = jobJson.getString((String)iter.next());
                String jobDescription = jobJson.getString((String)iter.next());
                int devNum = Integer.parseInt(devNumString);
                if(devNum <= 0){
                    return null;
                }

                currentJob.setJobDescription(jobDescription);
                currentJob.setDevQualification(devQualification);
                currentJob.setDevNum(devNum);
                allJobs.add(currentJob);
            }
        }catch (NumberFormatException ex){
            logger.warn("Some info parsing problem while adding order", ex);
            return null;
        }
        return allJobs;
    }
}
