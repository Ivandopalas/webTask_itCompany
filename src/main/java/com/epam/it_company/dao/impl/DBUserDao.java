package com.epam.it_company.dao.impl;

import com.epam.it_company.controller.UserType;
import com.epam.it_company.dao.DaoException;
import com.epam.it_company.dao.UserDao;
import com.epam.it_company.dao.pool.ConnectionPool;
import com.epam.it_company.dao.pool.ConnectionPoolException;
import com.epam.it_company.domain.UserParameters;

import java.sql.*;

public class DBUserDao implements UserDao {
    private static final DBUserDao dbUserDao= new DBUserDao();

    private static final String EXCEPTION_CONNECT_MESSAGE =
            "Can't get connection from connection pool";
    private static final String EXCEPTION_ACT_MESSAGE =
            "Sql actions exception";

    private final static String GET_LOGIN_PASS_QUERY =
            "SELECT login,password,type FROM itcompanybd.User" +
            " JOIN itcompanybd.User_type ON User.idUser=User_type.idUser;";
    private static final String USER_LOGIN_QUERY = "SELECT login FROM User;";

    private static final String REGISTRATION_QUERY =
            "INSERT INTO itcompanybd.User (name,login,password,email)" +
                    " VALUES ( ?, ?, ?, ?);";

    private static final String SET_USER_TYPE_QUERY =
            "INSERT INTO itcompanybd.User_type (type, idUser) " +
                    "SELECT ?,idUser FROM itcompanybd.User " +
                    "WHERE login = ?;";

    public static DBUserDao getInstance(){
        return dbUserDao;
    }

    @Override
    public String login(String login, String password) throws DaoException{
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            connection = pool.takeConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(GET_LOGIN_PASS_QUERY);
            while (resultSet.next()){
                String bdLogin = resultSet.getString(DatabaseColumnName.LOGIN);
                String bdPassword =  resultSet.getString(DatabaseColumnName.PASSWORD);
                if( bdLogin.equals(login) && bdPassword.equals(password)){
                    String userType = resultSet.getString(DatabaseColumnName.USER_TYPE);
                    return userType;
                }
            }
        }catch (ConnectionPoolException ex){
            throw new DaoException(EXCEPTION_CONNECT_MESSAGE,ex);
        }catch (SQLException ex){
            throw new DaoException(EXCEPTION_ACT_MESSAGE,ex);
        }finally {
            pool.closeConnection(connection,statement,resultSet);
        }
        return UserType.UNLOGINED.toString();
    }

    @Override
    public boolean registerClient(UserParameters regInfo) throws DaoException{
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = pool.takeConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(USER_LOGIN_QUERY);
            String incomeLogin = regInfo.getLogin();
            while (resultSet.next()){
                String userLoginInBd = resultSet.getString(DatabaseColumnName.USER_LOGIN);
                if(userLoginInBd.equalsIgnoreCase(incomeLogin)){
                    return false;
                }
            }
            registerUser(connection,regInfo);
        }catch (ConnectionPoolException ex){
            throw new DaoException(EXCEPTION_CONNECT_MESSAGE,ex);
        }catch (SQLException ex){
            throw new DaoException(EXCEPTION_ACT_MESSAGE,ex);
        }finally{
            if(resultSet != null){
                pool.closeConnection(connection,statement,resultSet);
            }else {
                pool.closeConnection(connection,statement);
            }
        }
        return true;
    }

    private void registerUser(Connection connection, UserParameters regInfo) throws SQLException{

        String incomeName = regInfo.getName();
        String incomeLogin = regInfo.getLogin();
        String incomePassword = regInfo.getPassword();
        String incomeEmail = regInfo.getEmail();

        connection.setAutoCommit(false);
        PreparedStatement prepStatementRegistration;
        prepStatementRegistration = connection.prepareStatement(REGISTRATION_QUERY);
        PreparedStatement prepStatementSetUserType;
        prepStatementSetUserType = connection.prepareStatement(SET_USER_TYPE_QUERY);
        prepStatementRegistration.setString(1, incomeName);
        prepStatementRegistration.setString(2, incomeLogin);
        prepStatementRegistration.setString(3, incomePassword);
        prepStatementRegistration.setString(4, incomeEmail);
        prepStatementRegistration.executeUpdate();

        prepStatementSetUserType.setString(1, regInfo.getUserType().toString());
        prepStatementSetUserType.setString(2, incomeLogin);
        prepStatementSetUserType.executeUpdate();

        connection.commit();
        prepStatementRegistration.close();
        prepStatementSetUserType.close();
        connection.setAutoCommit(true);
    }

}
