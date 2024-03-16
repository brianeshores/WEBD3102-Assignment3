package com.example.ordermanagement.db;

import com.example.ordermanagement.dao.ProductDao;
import com.example.ordermanagement.model.Product;
import com.example.ordermanagement.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDB implements ProductDao {

    private static final String INSERT_PRODUCT_SQL = "INSERT INTO products" +
            " (name, description, price, quantity, category) VALUES (?, ?, ?, ?, ?);";

    private static final String SELECT_PRODUCT_BY_ID = "SELECT product_id, name, description, price, quantity, category FROM products WHERE product_id =?"; // Changed 'id' to 'product_id'
    private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM products";
    private static final String DELETE_PRODUCT_BY_ID = "DELETE FROM products WHERE product_id = ?;"; // Changed 'id' to 'product_id'
    private static final String UPDATE_PRODUCT = "UPDATE products SET name = ?, description = ?, price = ?, quantity = ?, category = ? WHERE product_id = ?;"; //\
    private static final String UPDATE_PRODUCT_QUANTITY = "UPDATE products SET quantity = ? WHERE product_id = ?;";
// Changed 'id' to 'product_id'

    @Override
    public void insertProduct(Product product) throws SQLException {
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SQL)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getQuantity());
            preparedStatement.setString(5, product.getCategory());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JDBCUtils.printSQLException(exception);
        }
    }

    @Override
    public Product selectProduct(int productId) throws SQLException { // Changed parameter name 'id' to 'productId'
        Product product = null;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {
            preparedStatement.setInt(1, productId); // Use 'productId' instead of 'id'
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String category = rs.getString("category");
                product = new Product(productId, name, description, price, quantity, category); // Use 'productId' in constructor
            }
        }
        return product;
    }

    @Override
    public List<Product> selectAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String category = rs.getString("category");
                products.add(new Product(productId, name, description, price, quantity, category));
            }
        }
        return products;
    }

    @Override
    public boolean deleteProduct(int productId) throws SQLException { // Changed parameter name 'id' to 'productId'
        boolean rowDeleted;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_BY_ID)) {
            statement.setInt(1, productId); // Use 'productId' instead of 'id'
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean updateProduct(Product product) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getQuantity());
            statement.setString(5, product.getCategory());
            statement.setInt(6, product.getProductId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public void updateProductQuantity(int productId, int quantity) throws SQLException {

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_QUANTITY)) {

            statement.setInt(1, quantity);
            statement.setInt(2, productId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating product quantity failed, no rows affected.");
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
    }
}
