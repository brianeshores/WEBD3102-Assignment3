package com.example.ordermanagement.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.ordermanagement.dao.OrderDao;
import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.utils.JDBCUtils;

public class OrderDB implements OrderDao {

    private static final String INSERT_ORDER_SQL = "INSERT INTO orders" +
            "  (user_id, status) VALUES " + " (?, ?);";

    private static final String SELECT_ORDER_BY_ID = "SELECT order_id, user_id, order_date, status FROM orders WHERE order_id =?";
    private static final String SELECT_ALL_ORDERS = "SELECT * FROM orders";
    private static final String DELETE_ORDER_BY_ID = "DELETE FROM orders WHERE order_id = ?;";
    private static final String UPDATE_ORDER_STATUS = "UPDATE orders SET status = ? WHERE order_id = ?;";

    public OrderDB() {}

    @Override
    public void insertOrder(Order order) throws SQLException {
        try (Connection connection = JDBCUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER_SQL)) {
            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setString(2, order.getStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JDBCUtils.printSQLException(exception);
        }
    }

    @Override
    public Order selectOrder(long orderId) {
        Order order = null;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDER_BY_ID);) {
            preparedStatement.setLong(1, orderId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                int user_id = rs.getInt("user_id");
                String status = rs.getString("status");
                order = new Order(order_id, user_id, status);
            }
        } catch (SQLException exception) {
            JDBCUtils.printSQLException(exception);
        }
        return order;
    }

    @Override
    public List<Order> selectAllOrders() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ORDERS);) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                int user_id = rs.getInt("user_id");
                String status = rs.getString("status");
                orders.add(new Order(order_id, user_id, status));
            }
        } catch (SQLException exception) {
            JDBCUtils.printSQLException(exception);
        }
        return orders;
    }

    @Override
    public boolean deleteOrder(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = JDBCUtils.getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_ORDER_BY_ID);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean updateOrderStatus(Order order) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = JDBCUtils.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER_STATUS);) {
            statement.setString(1, order.getStatus());
            statement.setInt(2, order.getOrderId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
}
