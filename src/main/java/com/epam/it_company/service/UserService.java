package com.epam.it_company.service;

import com.epam.it_company.controller.UserType;
import com.epam.it_company.dao.AdminDao;
import com.epam.it_company.dao.DaoException;
import com.epam.it_company.dao.ManagerDao;
import com.epam.it_company.dao.UserDao;
import com.epam.it_company.dao.daofactory.DaoFactory;
import com.epam.it_company.domain.UserParameters;
import com.epam.it_company.service.util.UserSecurity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class UserService {
    private final static Logger logger = LogManager.getLogger(UserService.class);



    private static final String EXCEPTION_MESSAGE_USER_TYPE =
            "Can't get user type, dao problems";

    private static final String EXCEPTION_MESSAGE_DEV_GET =
            "Can't get developers from database, dao problems";

    private static final String EXCEPTION_MESSAGE_CLIENT_GET =
            "Can't get clients, dao problems";

    private static final String EXCEPTION_MESSAGE_USER_GET =
            "Can't get users, dao problems";

    private static final String EXCEPTION_MESSAGE_REG_USER = "can't reg new client,dao problems";
    private final static UserService instance = new UserService();

    private UserService(){}

    public static UserService getInstance(){
        return instance;
    }


    /**
     * Get all clients in database.
     * @return list with user parameters domain containing information of clients
     * @throws ServiceException
     */
    public List<UserParameters> getAllClients() throws ServiceException{
        try {
            ManagerDao clientDao = DaoFactory.getDaoFactory().getManagerDao();
            List<UserParameters> allClients = clientDao.getAllClient();
            return allClients;
        }catch (DaoException ex){
            throw new ServiceException(EXCEPTION_MESSAGE_CLIENT_GET,ex);
        }
    }
    /**
     * Get all users in database.
     * @return list with user parameters domain containing information of users
     * @throws ServiceException
     */
    public List<UserParameters> getAllUsers() throws ServiceException{
        try {
            AdminDao clientDao = DaoFactory.getDaoFactory().getAdminDao();
            List<UserParameters> allUsers = clientDao.getAllUsers();
            return allUsers;
        }catch (DaoException ex){
            throw new ServiceException(EXCEPTION_MESSAGE_USER_GET,ex);
        }
    }
    /**
     * Get all developers in database
     * @return list of user domain contain developers info
     * @throws ServiceException
     */
    public List<UserParameters> getAllDevelopers() throws ServiceException{
        try {
            ManagerDao clientDao = DaoFactory.getDaoFactory().getManagerDao();
            List<UserParameters> allDevelopers = clientDao.getAllDevelopers();
            return allDevelopers;
        }catch (DaoException ex){
            throw new ServiceException(EXCEPTION_MESSAGE_DEV_GET,ex);
        }
    }
    /**
     * Check income parameters in database and get type of current user if found in base.
     * @param loginInfo Bean with user information
     * @return type of income user
     * @throws ServiceException
     */
    public UserType getUserType(UserParameters loginInfo) throws ServiceException{
        UserType userType;
        try {
            UserSecurity security = UserSecurity.getInstance();
            String encodedPassword = security.encodePassword(loginInfo.getPassword());
            UserDao userDao = DaoFactory.getDaoFactory().getUserDao();

            String userTypeString = userDao.login(loginInfo.getLogin(),encodedPassword);
            userType = UserType.valueOf(userTypeString.toUpperCase());
        }catch (DaoException ex) {
            throw new ServiceException(EXCEPTION_MESSAGE_USER_TYPE,ex);
        }catch (IllegalArgumentException ex){
            logger.warn("Unknown user type, set unlogined",ex);
            return UserType.UNLOGINED;
        }
        return userType;
    }
    /**
     * Register new client in database.
     * @param regInfo Bean with all income registration information
     * @return true is registration was successful.
     * @throws ServiceException
     */
    public boolean registration(UserParameters regInfo) throws ServiceException{
        try {
            String encodedPassword;
            encodedPassword = UserSecurity.getInstance().encodePassword(regInfo.getPassword());
            DaoFactory daoFactory = DaoFactory.getDaoFactory();
            regInfo.setPassword(encodedPassword);
            boolean successReg = daoFactory.getUserDao().registerClient(regInfo);
            if(!successReg){
                return false;
            }
        }catch (DaoException ex){
            throw new ServiceException(EXCEPTION_MESSAGE_REG_USER, ex);
        }
        return true;
    }

    /**
     * Delete user from database.
     * @param userLogin login of user.
     * @return true if deleted and false otherwise.
     * @throws ServiceException
     */
    public boolean deleteDeveloper(String userLogin) throws ServiceException{
        try {
            AdminDao adminDao = DaoFactory.getDaoFactory().getAdminDao();
            boolean isDeleted = adminDao.deleteUser(userLogin);
            return isDeleted;
        }catch (DaoException ex){
            throw new ServiceException("", ex);
        }
    }
}
