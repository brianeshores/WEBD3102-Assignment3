package com.example.ordermanagement.dao;

import com.example.ordermanagement.model.User;
import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {
    void insertUser(User user) throws SQLException;
    boolean updateUser(User user) throws SQLException;
    boolean deleteUser(int userId) throws SQLException;
    Optional<User> selectUserById(int userId);
    Optional<User> authenticateUser(String email, String password);
}
