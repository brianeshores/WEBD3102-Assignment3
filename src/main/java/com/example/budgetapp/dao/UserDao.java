package com.example.budgetapp.dao;

import com.example.budgetapp.model.User;
import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {
    void insertUser(User user) throws SQLException;
    boolean updateUser(User user) throws SQLException;
    boolean deleteUser(long userId) throws SQLException;
    Optional<User> selectUserById(long userId);
    Optional<User> authenticateUser(String email, String password);
}
