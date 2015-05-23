package com.epam.it_company.dao.impl;

import com.epam.it_company.domain.Job;
import com.epam.it_company.domain.Order;
import com.epam.it_company.domain.UserParameters;
import com.epam.it_company.controller.UserType;
import com.epam.it_company.dao.ClientDao;
import com.epam.it_company.dao.DaoException;
import com.epam.it_company.dao.pool.ConnectionPool;
import com.epam.it_company.dao.pool.ConnectionPoolException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBClientDao implements ClientDao {
    private static final DBClientDao dbClientDao = new DBClientDao();

    private static final String EXCEPTION_CONNECT_MESSAGE =
            "Can't get connection from connection pool";
    private static final String EXCEPTION_ACT_MESSAGE =
            "Sql actions exception";

    private static final String USER_ID_QUERY =
            "SELECT idUser FROM itcompanybd.User WHERE login = ? ";

    private static final String ADD_ORDER_QUERY =
            "INSERT INTO itcompanybd.Order(name, idUser,isPaid,isConfirmed)" +
                    " VALUES (? , ?, '0', '0');";

    private static final String LAST_ORDER_ID_QUERY =
            "SELECT LAST_INSERT_ID() FROM itcompanybd.Order;";

    private static final String ADD_JOB_TO_ORDER_QUERY =
    "INSERT INTO itcompanybd.Jobs(description, qualification, developers_num, idOrder)"+
            " VALUES ( ?, ?, ?, ?);";

    private static final String ALL_USER_ORDER_ID_QUERY =
            "SELECT idOrder FROM itcompanybd.Order " +
                    "JOIN itcompanybd.User ON itcompanybd.Order.idUser = itcompanybd.User.idUser " +
                    "WHERE User.login = ?;";

    private static final String ORDER_INFO_BY_ID_QUERY =
            "SELECT Order.idOrder, Order.name,Order.isPaid,Order.isConfirmed, Order.cost, " +
            " Jobs.qualification,Jobs.developers_num,Jobs.description " +
            "FROM itcompanybd.Order JOIN itcompanybd.Jobs ON Order.idOrder = Jobs.idOrder " +
            "WHERE Order.idOrder = ?;";



    public static DBClientDao getInstance(){
        return dbClientDao;
    }

    @Override
    public boolean createOrder(Order order,String clientLogin) throws DaoException{
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int countRows;
        try {
            connection = pool.takeConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            PreparedStatement prepStatementIdUser;
            prepStatementIdUser = connection.prepareStatement(USER_ID_QUERY);
            prepStatementIdUser.setString(1, clientLogin);
            resultSet = prepStatementIdUser.executeQuery();

            if(resultSet.next()) {
                int idUser = resultSet.getInt(DatabaseColumnName.USER_ID);
                countRows = addOrder(connection,order.getName(),idUser);
                if(countRows == 0){
                    connection.setAutoCommit(true);
                    return false;
                }
            }
            List<Job> jobs = order.getJobList();
            resultSet = statement.executeQuery(LAST_ORDER_ID_QUERY);

            if(resultSet.next()) {
                int lastIdOrder = resultSet.getInt(1); // LAST_ORDER_ID()
                addJobsToOrder(connection,jobs,lastIdOrder);
            }
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

    @Override
    public List<Order> getOrderList(String userLogin) throws DaoException{

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = pool.takeConnection();
            statement = connection.createStatement();
            PreparedStatement prepStatementIdOrder;
            prepStatementIdOrder = connection.prepareStatement(ALL_USER_ORDER_ID_QUERY);
            prepStatementIdOrder.setString(1, userLogin);
            resultSet = prepStatementIdOrder.executeQuery();

            ArrayList<Integer> allUserIdOrders = new ArrayList<>();
            while (resultSet.next()){
                int currentOrderId = resultSet.getInt(DatabaseColumnName.ORDER_ID);
                allUserIdOrders.add(currentOrderId);
            }
            List<Order> allOrders;
            allOrders = getAllOrders(connection,allUserIdOrders);
            return allOrders;
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

    }


    private List<Order> getAllOrders(
            Connection connection, ArrayList<Integer> allUserIdOrders ) throws SQLException{

        ResultSet resultSet;
        List<Order> allOrders = new ArrayList<>();
        PreparedStatement prepStatementOrderInfo;
        prepStatementOrderInfo = connection.prepareStatement(ORDER_INFO_BY_ID_QUERY);
        for(Integer orderId: allUserIdOrders){
            prepStatementOrderInfo.setInt(1, orderId);
            resultSet = prepStatementOrderInfo.executeQuery();
            Order nextOrder = new Order();
            ArrayList<Job> jobList = new ArrayList<>();

            nextOrder.setJobList(jobList);
            boolean markToAddOrderInfoOnce = true;
            while (resultSet.next()){
                if(markToAddOrderInfoOnce) {
                    nextOrder.setOrderId(resultSet.getInt(DatabaseColumnName.ORDER_ID));
                    nextOrder.setName(resultSet.getString(DatabaseColumnName.ORDER_NAME));
                    nextOrder.setCost(resultSet.getInt(DatabaseColumnName.ORDER_COST));
                    nextOrder.setConfirmed(resultSet.getBoolean(DatabaseColumnName.ORDER_IS_CONFIRMED));
                    nextOrder.setPaid(resultSet.getBoolean(DatabaseColumnName.ORDER_IS_PAID));
                    markToAddOrderInfoOnce = false;
                }
                Job nextJob = new Job();
                nextJob.setDevNum(resultSet.getInt(DatabaseColumnName.DEVELOPERS_NUM_ON_JOB));
                nextJob.setDevQualification(
                        resultSet.getString(DatabaseColumnName.DEVELOPERS_QUALIFICATION));
                nextJob.setJobDescription(resultSet.getString(DatabaseColumnName.JOB_DESCRIPTION));
                nextOrder.getJobList().add(nextJob);
            }
            allOrders.add(nextOrder);
        }
        return allOrders;
    }

    private void addJobsToOrder(
            Connection connection, List<Job> jobs,int lastIdOrder) throws SQLException{

        PreparedStatement prepStatementAddJob;
        prepStatementAddJob = connection.prepareStatement(ADD_JOB_TO_ORDER_QUERY);
        for(Job i : jobs){
            String description = i.getJobDescription();
            String qualification = i.getDevQualification();
            int devNum = i.getDevNum();
            prepStatementAddJob.setString(1, description);
            prepStatementAddJob.setString(2, qualification);
            prepStatementAddJob.setInt(3, devNum);
            prepStatementAddJob.setInt(4, lastIdOrder);
            prepStatementAddJob.executeUpdate();
        }
    }

    private int addOrder(Connection connection, String orderName,int idUser) throws SQLException{

        PreparedStatement prepStatementAddOrder;
        prepStatementAddOrder = connection.prepareStatement(ADD_ORDER_QUERY);
        prepStatementAddOrder.setString(1, orderName);
        prepStatementAddOrder.setInt(2, idUser);
        int countRows = prepStatementAddOrder.executeUpdate();
        return countRows;
    }
}
