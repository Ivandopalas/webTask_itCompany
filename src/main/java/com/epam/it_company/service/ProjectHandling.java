package com.epam.it_company.service;

import com.epam.it_company.dao.DaoException;
import com.epam.it_company.dao.DeveloperDao;
import com.epam.it_company.dao.ManagerDao;
import com.epam.it_company.dao.daofactory.DaoFactory;
import com.epam.it_company.domain.Project;
import com.epam.it_company.domain.UserParameters;

import java.util.List;

public class ProjectHandling {
    private final static String EXCEPTION_MESSAGE_SET_TIME =
            "Can't set time on project, dao problems";

    private static final String EXCEPTION_MESSAGE_ADD_ACT =
            "Can't add new project, dao problems";
    private static final String EXCEPTION_MESSAGE_GET_ACT =
            "Can't get projects from database";

    private final static ProjectHandling instance = new ProjectHandling();

    private ProjectHandling(){}


    public static ProjectHandling getInstance(){
        return instance;
    }

    /**
     * Add new project in database keeping there linked with order, on which project is bases
     * and manager, who created project.
     * @param orderId id of order in which project is bases
     * @param managerLogin login of manager who created project
     * @param developerList list of developers that manager organized on current project
     * @param projectName name of project
     * @param projectCost final cost of project that client should paid.
     * @return true if project set successfully otherwise
     * @throws ServiceException
     */
    public boolean addProject(
            int orderId, String managerLogin,
            List<UserParameters> developerList,
            String projectName,int projectCost) throws ServiceException{

        try {
            ManagerDao managerDao = DaoFactory.getDaoFactory().getManagerDao();
            boolean isSuccess = managerDao.createProject(
                    orderId,managerLogin,developerList,projectName,projectCost);
            return isSuccess;
        }catch (DaoException ex){
            throw new ServiceException(EXCEPTION_MESSAGE_ADD_ACT, ex);
        }
    }
    /**
     * Increase working time on project of current user.
     * @param developerLogin Current client on project
     * @param projectId Project id
     * @param time Increasing time
     * @return true if time increased in database and false if no changes in database.
     * @throws ServiceException
     */
    public boolean increaseTimeOnProject(
            String developerLogin,int projectId,int time) throws ServiceException{
        try {
            DeveloperDao developerDao = DaoFactory.getDaoFactory().getDeveloperDao();
            boolean isSuccess = developerDao.addWorkingTimeOnProject(
                    developerLogin,projectId,time);
            return isSuccess;
        }catch (DaoException ex){
            throw new ServiceException(EXCEPTION_MESSAGE_SET_TIME,ex);
        }
    }

    /**
     * Get list of all developer's projects
     * @param developerLogin developer login whose projects will return
     * @return list of project domain
     * @throws ServiceException
     */
    public List<Project> getProjects(String developerLogin) throws ServiceException{
        try {
            DeveloperDao developerDao = DaoFactory.getDaoFactory().getDeveloperDao();
            List<Project> allProjects = developerDao.getProjects(developerLogin);
            return allProjects;
        }catch (DaoException ex){
            throw new ServiceException(EXCEPTION_MESSAGE_GET_ACT,ex);
        }
    }
}
