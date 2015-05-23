package com.epam.it_company.dao;

import com.epam.it_company.domain.Order;
import com.epam.it_company.domain.UserParameters;

import java.util.List;

/**
 * The Client Data Access Object is the interface providing access to client data and
 * all relations with client.
 */
public interface ClientDao {

    /**
     * Add new order in database. Connect order with client.
     * @param order bead contain all order information.
     * @param login client login who created this order.
     * @return true is order added in database successfully. False otherwise.
     * @throws DaoException
     */
    public boolean createOrder(Order order,String login) throws DaoException;

    /**
     * Get all orders which assigned to certain client.
     * @param clientName Client, who's orders you'll get.
     * @return list of bean which contain information about orders.
     * @throws DaoException
     */
    public List<Order> getOrderList(String clientName) throws DaoException;

}
