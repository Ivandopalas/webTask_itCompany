package com.epam.it_company.dao;

import com.epam.it_company.controller.UserType;
import com.epam.it_company.dao.daofactory.DaoFactory;
import com.epam.it_company.dao.pool.ConnectionPool;
import com.epam.it_company.service.util.UserSecurity;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.Assert.assertEquals;

public class UserDaoTest {

    private static final String ADD_TEST_USER_QUERY =
            "INSERT INTO itcompanybd.User (login, password,name,email)" +
                    " VALUES ( ?, ?, ?, ?);";
    private static final String DEL_TEST_USER_QUERY =
            "DELETE FROM itcompanybd.User" +
                    " WHERE login = ?;";
    private static final String SET_USER_TYPE_QUERY =
            "INSERT INTO itcompanybd.User_type (type, idUser) " +
                    "SELECT ?,idUser FROM itcompanybd.User " +
                    "WHERE login = ?;";

    private UserDao testUserDao;

    @Test
    public void loginTest() throws Exception{
        this.testUserDao = DaoFactory.getDaoFactory().getUserDao();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.takeConnection();

        UserSecurity serviceInstance = UserSecurity.getInstance();
        String encodedPass =  serviceInstance.encodePassword("pass");

        String managerTestName = "Test_Manager";
        addUser(connection, encodedPass, managerTestName, UserType.MANAGER);
        String incomeStringTypeManager = testUserDao.login(managerTestName, encodedPass);
        UserType testTypeManager = UserType.valueOf(incomeStringTypeManager.toUpperCase());
        deleteUser(connection,managerTestName);
        assertEquals(UserType.MANAGER, testTypeManager);

        String clientTestName = "Test_Client";
        addUser(connection, encodedPass, clientTestName, UserType.CLIENT);
        String incomeStringTypeClient = testUserDao.login(clientTestName, encodedPass);
        UserType testTypeClient = UserType.valueOf(incomeStringTypeClient.toUpperCase());
        deleteUser(connection,clientTestName);
        assertEquals(UserType.CLIENT, testTypeClient);

        String developerTestName = "Test_Developer";
        addUser(connection, encodedPass, developerTestName, UserType.DEVELOPER);
        String incomeStringTypeDeveloper = testUserDao.login(developerTestName, encodedPass);
        UserType testTypeDeveloper = UserType.valueOf(incomeStringTypeDeveloper.toUpperCase());
        deleteUser(connection,developerTestName);
        assertEquals(UserType.DEVELOPER, testTypeDeveloper);

        String unknownTestName = "Test_Unknown_user";
        String incomeStringTypeUnknown = testUserDao.login(unknownTestName, encodedPass);
        UserType testTypeUnlogined = UserType.valueOf(incomeStringTypeUnknown.toUpperCase());
        assertEquals(UserType.UNLOGINED, testTypeUnlogined);

        connection.close();
    }
    private void deleteUser(Connection connection,String testLogin) throws Exception{

        PreparedStatement prepStateDelUser = connection.prepareStatement(DEL_TEST_USER_QUERY);
        prepStateDelUser.setString(1,testLogin);
        prepStateDelUser.executeUpdate();
    }

    private void addUser(Connection connection,String encodedPass,
                                String testLogin, UserType userType) throws Exception{

        PreparedStatement prepStateAddUser = connection.prepareStatement(ADD_TEST_USER_QUERY);
        prepStateAddUser.setString(1,testLogin);
        prepStateAddUser.setString(2,encodedPass);
        prepStateAddUser.setString(3,"name");
        prepStateAddUser.setString(4,"email");
        prepStateAddUser.executeUpdate();

        PreparedStatement prepStateSetType = connection.prepareStatement(SET_USER_TYPE_QUERY);
        prepStateSetType.setString(1, userType.toString());
        prepStateSetType.setString(2, testLogin);
        prepStateSetType.executeUpdate();
    }

}
