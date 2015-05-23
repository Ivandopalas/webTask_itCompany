package com.epam.it_company.dao.impl;

import com.epam.it_company.domain.Project;
import com.epam.it_company.dao.DaoException;
import com.epam.it_company.dao.DeveloperDao;
import com.epam.it_company.dao.pool.ConnectionPool;
import com.epam.it_company.dao.pool.ConnectionPoolException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBDeveloperDao implements DeveloperDao {
    private static final DBDeveloperDao dbDeveloperDao = new DBDeveloperDao();

    private static final String EXCEPTION_CONNECT_MESSAGE =
            "Can't get connection from connection pool";
    private static final String EXCEPTION_ACT_MESSAGE =
            "Sql actions exception";

    private static final String GET_DEVS_PROJECTS_QUERY =
            "SELECT Project.name,Project.idProject FROM itcompanybd.Project JOIN " +
                    "itcompanybd.Developers_on_project ON Project.idProject" +
                    " = Developers_on_project.idProject " +
                    "WHERE Developers_on_project.idUser = " +
                    "(SELECT User.idUser from itcompanybd.User where login = ?);";

    private static final String INSERT_WORKING_TIME_QUERY =
            "INSERT itcompanybd.Developer_working_time(hours_on_project,idProject,idUser) " +
                    "VALUES ( ?, ?, ?);";

    private static final String UPDATE_WORKING_TIME_QUERY =
            "UPDATE itcompanybd.Developer_working_time " +
                    "SET  Developer_working_time.hours_on_project = " +
                    "Developer_working_time.hours_on_project + ? " +
                    "WHERE Developer_working_time.idProject = ? AND " +
                    "Developer_working_time.idUser = ?;";
    private static final String GET_PROJECT_TIME =
            "SELECT hours_on_project FROM itcompanybd.Developer_working_time " +
                    "WHERE Developer_working_time.idUser = " +
                    "(SELECT idUser FROM itcompanybd.User " +
                    "WHERE User.login = ?) AND idProject = ?;";
    private static final String CHECK_IS_DEV_WORKED_QUERY =
            "SELECT * FROM itcompanybd.Developer_working_time " +
                    "WHERE Developer_working_time.idUser = ? AND " +
                    "Developer_working_time.idProject = ?;";

    private static final String USER_ID_QUERY =
            "SELECT idUser FROM itcompanybd.User WHERE login = ? ";
    public static DBDeveloperDao getInstance(){
        return dbDeveloperDao;
    }

    @Override
    public List<Project> getProjects(String developerLogin) throws DaoException {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Project> developerProjects = new ArrayList<>();
        try {
            connection = pool.takeConnection();
            statement = connection.createStatement();

            PreparedStatement prepStatementDevsProjs;
            prepStatementDevsProjs = connection.prepareStatement(GET_DEVS_PROJECTS_QUERY);
            prepStatementDevsProjs.setString(1,developerLogin);
            resultSet = prepStatementDevsProjs.executeQuery();

            while (resultSet.next()) {
                Project currentProject = new Project();
                String projectName = resultSet.getString(DatabaseColumnName.PROJECT_NAME);
                int projectId = resultSet.getInt(DatabaseColumnName.PROJECT_ID);
                int projectTime = getTimeOnProject(connection,developerLogin,projectId);

                currentProject.setTimeOnProject(projectTime);
                currentProject.setProjectName(projectName);
                currentProject.setIdProject(projectId);
                developerProjects.add(currentProject);
            }

            prepStatementDevsProjs.close();
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
        return developerProjects;
    }

    @Override
    public boolean addWorkingTimeOnProject(
            String developerLogin, int projectId, int time) throws DaoException {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = pool.takeConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            PreparedStatement prepStatementidUser;
            prepStatementidUser = connection.prepareStatement(USER_ID_QUERY);
            prepStatementidUser.setString(1,developerLogin);
            resultSet = prepStatementidUser.executeQuery();
            int idDeveloper;

            if(resultSet.next()) {
                idDeveloper = resultSet.getInt(DatabaseColumnName.USER_ID);
                addTimeOnProject(connection,idDeveloper,projectId,time);
            }else{
                return false;
            }
            connection.commit();
            connection.setAutoCommit(true);
            prepStatementidUser.close();
        }catch (ConnectionPoolException ex){
            throw new DaoException(EXCEPTION_CONNECT_MESSAGE,ex);
        }catch (SQLException ex){
            throw new DaoException(EXCEPTION_ACT_MESSAGE,ex);
        }finally{
            pool.closeConnection(connection,statement,resultSet);
        }
        return true;
    }

    private int getTimeOnProject(
            Connection connection, String devLogin,int idProject) throws SQLException{

        PreparedStatement prepStatementWorkingTime;
        prepStatementWorkingTime = connection.prepareStatement(GET_PROJECT_TIME);

        prepStatementWorkingTime.setString(1, devLogin);
        prepStatementWorkingTime.setInt(2, idProject);
        ResultSet resultSet;
        resultSet = prepStatementWorkingTime.executeQuery();
        int time = -1;
        if(resultSet.next()) {
            time = resultSet.getInt(DatabaseColumnName.HOURS_ON_PROJECT);
        }
        return time;
    }
    private void addTimeOnProject(Connection connection,
                         int idDeveloper,int projectId, int time) throws SQLException{

        ResultSet resultSet;
        PreparedStatement prepStatementInsertTime;
        prepStatementInsertTime = connection.prepareStatement(INSERT_WORKING_TIME_QUERY);
        PreparedStatement prepStatementUpdateTime;
        prepStatementUpdateTime = connection.prepareStatement(UPDATE_WORKING_TIME_QUERY);

        PreparedStatement prepStatementCheckIsWorked;
        prepStatementCheckIsWorked = connection.prepareStatement(CHECK_IS_DEV_WORKED_QUERY);
        prepStatementCheckIsWorked.setInt(1, idDeveloper);
        prepStatementCheckIsWorked.setInt(2, projectId);
        resultSet = prepStatementCheckIsWorked.executeQuery();
        if(resultSet.next()) {
            prepStatementUpdateTime.setInt(1, time);
            prepStatementUpdateTime.setInt(2, projectId);
            prepStatementUpdateTime.setInt(3, idDeveloper);

            prepStatementUpdateTime.executeUpdate();
        }else {
            prepStatementInsertTime.setInt(1, time);
            prepStatementInsertTime.setInt(2, projectId);
            prepStatementInsertTime.setInt(3, idDeveloper);

            prepStatementInsertTime.executeUpdate();
        }
        prepStatementCheckIsWorked.close();
        prepStatementUpdateTime.close();
        prepStatementInsertTime.close();
        resultSet.close();
    }
}
