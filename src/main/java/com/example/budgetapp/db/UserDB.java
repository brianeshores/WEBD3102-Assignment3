package com.example.budgetapp.db;

import com.example.budgetapp.dao.UserDao;
import com.example.budgetapp.model.User;
import com.example.budgetapp.utils.JDBCUtils;

import java.sql.*;
import java.util.Optional;

public class UserDB implements UserDao {

    private static final String INSERT_USER_SQL = "INSERT INTO Users (userName, email, password) VALUES (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "SELECT userID, userName, email, password FROM Users WHERE userID = ?";
    private static final String UPDATE_USER_SQL = "UPDATE Users SET email = ?, password = ?, userName = ? WHERE userID = ?;";
    private static final String DELETE_USER_SQL = "DELETE FROM Users WHERE userID = ?;";
    private static final String AUTHENTICATE_USER_SQL = "SELECT userID, userName, email, password FROM Users WHERE email = ? AND password = ?;";

    @Override
    public void insertUser(User user) throws SQLException {
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_SQL)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUserName());
            statement.setLong(4, user.getUserId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public boolean deleteUser(long userId) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_SQL)) {
            statement.setLong(1, userId);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public Optional<User> selectUserById(long userId) {
        Optional<User> user = Optional.empty();
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setLong(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("userID");
                String userName = rs.getString("userName");
                String email = rs.getString("email");
                String password = rs.getString("password");
                user = Optional.of(new User(id ,userName, email, password));
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
        return user;
    }

    @Override
    public Optional<User> authenticateUser(String email, String password) {
        System.out.println("authenticating user");
        Optional<User> user = Optional.empty();
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(AUTHENTICATE_USER_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("userID");
                String useremail = rs.getString("email");
                String userPassword = rs.getString("password");
                String userName = rs.getString("userName");
                user = Optional.of(new User(userId, userName, useremail, userPassword));
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
        return user;
    }
}