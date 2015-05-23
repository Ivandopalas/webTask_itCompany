package com.epam.it_company.dao.impl;

import com.epam.it_company.controller.UserType;
import com.epam.it_company.dao.AdminDao;
import com.epam.it_company.dao.DaoException;
import com.epam.it_company.dao.pool.ConnectionPool;
import com.epam.it_company.dao.pool.ConnectionPoolException;
import com.epam.it_company.domain.UserParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBAdminDao implements AdminDao {
    private final static Logger logger = LogManager.getLogger(DBAdminDao.class);


    private static final DBAdminDao dbAdminDao = new DBAdminDao();

    private static final String EXCEPTION_CONNECT_MESSAGE =
            "Can't get connection from connection pool";
    private static final String EXCEPTION_ACT_MESSAGE =
            "Sql actions exception";

    private static final String GET_USERS_QUERY =
            "SELECT login,name,email,type FROM itcompanybd.User " +
                    "JOIN itcompanybd.User_type ON User_type.idUser = User.idUser " +
                    "WHERE type <> 'ADMIN';";

    private static final String DELETE_USER =
            "DELETE from itcompanybd.User WHERE login = ?;";

    public static DBAdminDao getInstance(){
        return dbAdminDao;
    }

    @Override
    public List<UserParameters> getAllUsers() throws DaoException{

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = pool.takeConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(GET_USERS_QUERY);
            List<UserParameters> allUsers = new ArrayList<>();
            while (resultSet.next()) {
                UserParameters currentUser = new UserParameters();
                String userLogin = resultSet.getString(DatabaseColumnName.LOGIN);
                String userName = resultSet.getString(DatabaseColumnName.USER_NAME);
                String userEmail = resultSet.getString(DatabaseColumnName.EMAIL);
                String userTypeLine = resultSet.getString(DatabaseColumnName.USER_TYPE);
                UserType userType;
                try {
                    userType = UserType.valueOf(userTypeLine.toUpperCase());
                }catch (IllegalArgumentException ex) {
                    logger.warn("Can't parse user type, set unknown");
                    userType = UserType.UNKNOWN;
                }
                currentUser.setUserType(userType);
                currentUser.setLogin(userLogin);
                currentUser.setName(userName);
                currentUser.setEmail(userEmail);
                allUsers.add(currentUser);
            }
            return allUsers;
        }catch (ConnectionPoolException ex){
            throw new DaoException(EXCEPTION_CONNECT_MESSAGE,ex);
        }catch (SQLException ex){
            throw new DaoException(EXCEPTION_ACT_MESSAGE,ex);
        }finally{
            pool.closeConnection(connection,statement,resultSet);
        }
    }

    /**
     * Delete any user from database.
     * @param userLogin Login of user.
     * @return true if deleted and false otherwise.
     * @throws DaoException
     */
    @Override
    public boolean deleteUser(String userLogin) throws DaoException{

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = pool.takeConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(GET_USERS_QUERY);
            PreparedStatement prepStatementDeleteUser;
            prepStatementDeleteUser = connection.prepareStatement(DELETE_USER);
            prepStatementDeleteUser.setString(1, userLogin);

            int rowEff;
            rowEff = prepStatementDeleteUser.executeUpdate();
            if(rowEff != 0){
                return true;
            }
            return false;
        }catch (ConnectionPoolException ex){
            throw new DaoException(EXCEPTION_CONNECT_MESSAGE,ex);
        }catch (SQLException ex){
            throw new DaoException(EXCEPTION_ACT_MESSAGE,ex);
        }finally{
            pool.closeConnection(connection,statement,resultSet);
        }
    }

}
