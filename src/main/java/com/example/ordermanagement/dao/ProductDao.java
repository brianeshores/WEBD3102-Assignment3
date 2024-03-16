package com.example.ordermanagement.dao;

import com.example.ordermanagement.model.Product;
import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    void insertProduct(Product product) throws SQLException;
    boolean updateProduct(Product product) throws SQLException;

    boolean deleteProduct(int productId) throws SQLException;

    Product selectProduct(int productId) throws SQLException;
    List<Product> selectAllProducts() throws SQLException;

    void updateProductQuantity(int productId, int quantity) throws SQLException;

}
