package com.example.ordermanagement.dao;

import com.example.ordermanagement.model.Order;
import java.sql.SQLException;
import java.util.List;

public interface OrderDao {
    void insertOrder(Order order) throws SQLException;

    Order selectOrder(long orderId);

    List<Order> selectAllOrders();

    boolean deleteOrder(int id) throws SQLException;

    boolean updateOrderStatus(Order order) throws SQLException;
}
