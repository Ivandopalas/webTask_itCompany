package com.epam.it_company.dao.daofactory;

import com.epam.it_company.dao.*;
import com.epam.it_company.dao.DaoPropertyFileParameters;
import com.epam.it_company.dao.util.DaoResourceManager;

/**
 * Abstract dao factory class. Allow to get any dao factory depending on type.
 */
public abstract class DaoFactory {

    private static final String FACTORY_BD_TYPE = "bd";
    private static final String EXCEPTION_FACTORY_MESSAGE =
            "Can't get any data access object";

    public abstract AdminDao getAdminDao();
    public abstract ClientDao getClientDao();
    public abstract DeveloperDao getDeveloperDao();
    public abstract ManagerDao getManagerDao();
    public abstract UserDao getUserDao();

    /**
     * @return dao factory of certain type.
     * @throws DaoException
     */
    public static DaoFactory getDaoFactory() throws DaoException{

        String type = DaoResourceManager.getInstance().getValue(
                DaoPropertyFileParameters.DAO_TYPE);

        switch (type){
            case FACTORY_BD_TYPE:
                return DBDaoFactory.getInstance();
            default:
                throw new DaoException(EXCEPTION_FACTORY_MESSAGE);
        }
    }
}