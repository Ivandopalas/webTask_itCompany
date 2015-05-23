package com.epam.it_company.dao.impl;

import com.epam.it_company.domain.UserParameters;
import com.epam.it_company.controller.UserType;
import com.epam.it_company.dao.DaoException;
import com.epam.it_company.dao.ManagerDao;
import com.epam.it_company.dao.pool.ConnectionPool;
import com.epam.it_company.dao.pool.ConnectionPoolException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManagerDao implements ManagerDao {
    private static final DBManagerDao dbManagerDao = new DBManagerDao();

    private static final String EXCEPTION_CONNECT_MESSAGE =
            "Can't get connection from connection pool";
    private static final String EXCEPTION_ACT_MESSAGE =
            "Sql actions exception";

    private static final String GET_CLIENTS_QUERY =
            "SELECT login,name,email FROM itcompanybd.User " +
                    "JOIN itcompanybd.User_type ON User_type.idUser = User.idUser " +
                    "WHERE User_type.type = 'client'";
    private static final String GET_DEVELOPERS_QUERY =
            "SELECT login,name,email FROM itcompanybd.User " +
                    "JOIN itcompanybd.User_type ON User_type.idUser = User.idUser " +
                    "WHERE User_type.type = 'developer'";
    private static final String CREATE_PROJECT_QUERY =
            "INSERT INTO itcompanybd.Project(name, idOrder,idUser)" +
                    " VALUES (?, ?, ?);";
    private static final String ID_USER_QUERY =
            "SELECT idUser FROM itcompanybd.User WHERE login = ? ";
    private static final String LAST_PROJECT_ID_QUERY =
            "SELECT LAST_INSERT_ID() FROM itcompanybd.Project;";
    private static final String ADD_DEV_ON_PROJ_QUERY =
            "INSERT INTO itcompanybd.Developers_on_project(idUser,idProject) " +
                    "VALUES (?, ?);";
    private static final String UPDATE_ORDER_QUERY =
            "UPDATE itcompanybd.Order " +
                    "SET Order.cost = ? ,Order.isConfirmed = '1' " +
                    "WHERE Order.idOrder = ?;";

    public static DBManagerDao getInstance(){
        return dbManagerDao;
    }

    @Override
    public List<UserParameters> getAllClient() throws DaoException {
        return getAllUsers(UserType.CLIENT);
    }

    @Override
    public List<UserParameters> getAllDevelopers()  throws DaoException{
        return getAllUsers(UserType.DEVELOPER);
    }

    @Override
    public boolean createProject(
            int idOrder, String managerLogin,
            List<UserParameters> developerList,
            String projectName, int projectCost) throws DaoException {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = pool.takeConnection();
            statement = connection.createStatement();
            PreparedStatement prepStatementIdUser;
            prepStatementIdUser = connection.prepareStatement(ID_USER_QUERY);
            prepStatementIdUser.setString(1, managerLogin);
            resultSet = prepStatementIdUser.executeQuery();

            int idManager;
            connection.setAutoCommit(false);
            if(resultSet.next()) {
                idManager = resultSet.getInt(DatabaseColumnName.USER_ID);
                registerProjectInfoInDatabase(connection,projectName,idOrder,idManager);
            }else{
                connection.setAutoCommit(true);
                return false;
            }
            int projectId;
            resultSet = statement.executeQuery(LAST_PROJECT_ID_QUERY);
            if(resultSet.next()){
                projectId = resultSet.getInt(1); //LAST_PROJECT_ID()
            }else{
                connection.setAutoCommit(true);
                return false;
            }
            updateProjectDependencies(connection, developerList,
                    projectId, projectCost, idOrder);
            connection.commit();
            connection.setAutoCommit(true);
            prepStatementIdUser.close();
        }catch (ConnectionPoolException ex){
            throw new DaoException(EXCEPTION_CONNECT_MESSAGE,ex);
        }catch (SQLException ex){
            throw new DaoException(EXCEPTION_ACT_MESSAGE,ex);
        }finally{
            pool.closeConnection(connection,statement,resultSet);
        }
        return true;
    }

    private void registerProjectInfoInDatabase(
            Connection connection, String projectName, int idOrder,
            int idManager) throws SQLException{

        PreparedStatement prepStateCreateProj;
        prepStateCreateProj = connection.prepareStatement(CREATE_PROJECT_QUERY);

        prepStateCreateProj.setString(1, projectName);
        prepStateCreateProj.setInt(2, idOrder);
        prepStateCreateProj.setInt(3, idManager);
        prepStateCreateProj.executeUpdate();
        prepStateCreateProj.close();
    }
    private void updateProjectDependencies(
            Connection connection,
            List<UserParameters> developerList,int projectId,
            int projectCost,int idOrder) throws SQLException{

        PreparedStatement prepStatementIdUser;
        prepStatementIdUser = connection.prepareStatement(ID_USER_QUERY);
        PreparedStatement prepStateAddDevOnProj;
        prepStateAddDevOnProj = connection.prepareStatement(ADD_DEV_ON_PROJ_QUERY);
        int developerId;

        for(UserParameters currentDeveloper: developerList){
            prepStatementIdUser.setString(1, currentDeveloper.getLogin());
            ResultSet resultSet = prepStatementIdUser.executeQuery();
            if(resultSet.next()){
                developerId = resultSet.getInt(DatabaseColumnName.USER_ID);
                prepStateAddDevOnProj.setInt(1,developerId);
                prepStateAddDevOnProj.setInt(2,projectId);
                prepStateAddDevOnProj.executeUpdate();
            }
        }
        PreparedStatement prepStateUpdateOrder;
        prepStateUpdateOrder = connection.prepareStatement(UPDATE_ORDER_QUERY);

        prepStateUpdateOrder.setInt(1, projectCost);
        prepStateUpdateOrder.setInt(2, idOrder);
        prepStateUpdateOrder.executeUpdate();
        prepStateUpdateOrder.close();
        prepStateAddDevOnProj.close();
    }

    private List<UserParameters> getAllUsers(UserType userType)throws DaoException{

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = pool.takeConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            if(userType == UserType.CLIENT) {
                resultSet = statement.executeQuery(GET_CLIENTS_QUERY);
            }
            if(userType == UserType.DEVELOPER){
                resultSet = statement.executeQuery(GET_DEVELOPERS_QUERY);
            }
            List<UserParameters> allUsers = new ArrayList<>();
            while (resultSet.next()) {
                UserParameters currentUser = new UserParameters();
                String userLogin = resultSet.getString(DatabaseColumnName.LOGIN);
                String userName = resultSet.getString(DatabaseColumnName.USER_NAME);
                String userEmail = resultSet.getString(DatabaseColumnName.EMAIL);
                currentUser.setLogin(userLogin);
                currentUser.setName(userName);
                currentUser.setEmail(userEmail);
                allUsers.add(currentUser);
            }
            connection.setAutoCommit(true);
            return allUsers;
        }catch (ConnectionPoolException ex){
            throw new DaoException(EXCEPTION_CONNECT_MESSAGE,ex);
        }catch (SQLException ex){
            throw new DaoException(EXCEPTION_ACT_MESSAGE,ex);
        }finally{
            pool.closeConnection(connection,statement,resultSet);
        }
    }
}
