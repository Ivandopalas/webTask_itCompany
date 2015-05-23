package com.epam.it_company.controller;

import com.epam.it_company.command.CommandException;
import com.epam.it_company.command.ICommand;
import com.epam.it_company.command.util.CommandHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main controller handles income command and execute them. Redirect to pages.
 */
public class Controller extends HttpServlet {
    private final static Logger logger = LogManager.getLogger(Controller.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String incomeAction = request.getParameter(RequestParameterName.ACTION);
        String resultPage;
        try {
            resultPage = executeIncomeAction(request, incomeAction);
        }catch (CommandException ex){
            logger.error("Command executing error", ex);
            resultPage = JspName.ERROR_PAGE;
        }
        forwardPage(request, response, resultPage);
    }


    private String executeIncomeAction(
            HttpServletRequest request, String action) throws CommandException{
        String resultPage;
        ICommand command = CommandHelper.getInstance().getCommand(action);
        resultPage = command.execute(request);
        return resultPage;
    }
    private void forwardPage(HttpServletRequest request,
                             HttpServletResponse response,
                             String page) throws ServletException,IOException{
        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        if(dispatcher != null){
            dispatcher.forward(request,response);
        }else{
     //       updatePage(request,response);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        forwardPage(request,response,JspName.ACCESS_DENIED_PAGE);
    }
}
