package com.epam.it_company.service;

import com.epam.it_company.dao.ClientDao;
import com.epam.it_company.dao.DaoException;

import com.epam.it_company.dao.daofactory.DaoFactory;
import com.epam.it_company.domain.Order;

import java.util.List;


public class OrderHandling {
    private static final String EXCEPTION_MESSAGE_ADD_ACT =
            "can't add new order, dao problems";
    private static final String EXCEPTION_MESSAGE_GET_ACT =
            "can't get client order list, dao problems";

    private final static OrderHandling instance = new OrderHandling();

    private OrderHandling(){}

    public static OrderHandling getInstance(){
        return instance;
    }
    /**
     * Add new order in database keeping there linked with current client,
     * who created this order.
     * @param order domain contain information about order.
     * @param currentUser login of client who created this order.
     * @return true if order successfully added in database.
     * @throws com.epam.it_company.service.ServiceException
     */
    public boolean addOrder(Order order,String currentUser) throws ServiceException {
        try {
            ClientDao clientDao = DaoFactory.getDaoFactory().getClientDao();
            boolean isSuccess = clientDao.createOrder(order, currentUser);
            return isSuccess;
        }catch (DaoException ex){
            throw new ServiceException(EXCEPTION_MESSAGE_ADD_ACT,ex);
        }
    }

    /**
     * Get all orders according certain client
     * @param clientLogin login of client that orders you want to get
     * @return list of domain with information about certain orders
     * @throws com.epam.it_company.service.ServiceException
     */
    public List<Order> getAllOrders(String clientLogin) throws ServiceException {
        try {
            ClientDao clientDao = DaoFactory.getDaoFactory().getClientDao();
            List<Order> allOrders = clientDao.getOrderList(clientLogin);
            return allOrders;
        }catch (DaoException ex){
            throw new ServiceException(EXCEPTION_MESSAGE_GET_ACT,ex);
        }
    }
}
