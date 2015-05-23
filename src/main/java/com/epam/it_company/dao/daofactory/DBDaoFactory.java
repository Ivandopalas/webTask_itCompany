package com.epam.it_company.dao.daofactory;

import com.epam.it_company.dao.*;
import com.epam.it_company.dao.impl.*;

/**
 * Dao factory realization for data base data access object.
 */
public class DBDaoFactory extends DaoFactory {
    private static final DBDaoFactory dbDaoFactory = new DBDaoFactory();

    public static DBDaoFactory getInstance(){
        return dbDaoFactory;
    }

    @Override
    public AdminDao getAdminDao() {
        return DBAdminDao.getInstance();
    }

    @Override
    public ClientDao getClientDao() {
        return DBClientDao.getInstance();
    }

    @Override
    public DeveloperDao getDeveloperDao() {
        return DBDeveloperDao.getInstance();
    }

    @Override
    public ManagerDao getManagerDao() {
        return DBManagerDao.getInstance();
    }

    @Override
    public UserDao getUserDao() { return DBUserDao.getInstance();    }
}
