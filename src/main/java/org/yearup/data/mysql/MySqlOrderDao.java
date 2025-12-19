package org.yearup.data.mysql;

import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;

import javax.sql.DataSource;

public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {
    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Order create(Order order) {
        return null;
    }

    @Override
    public OrderLineItem addOrderLineItem(OrderLineItem lineItem) {
        return null;
    }

    @Override
    public Order getById(int orderId) {
        return null;
    }
}
