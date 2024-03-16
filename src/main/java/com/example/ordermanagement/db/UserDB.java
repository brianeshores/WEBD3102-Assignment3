package com.example.ordermanagement.db;

import com.example.ordermanagement.dao.UserDao;
import com.example.ordermanagement.model.User;
import com.example.ordermanagement.utils.JDBCUtils;

import java.sql.*;
import java.util.Optional;

public class UserDB implements UserDao {

    private static final String INSERT_USER_SQL = "INSERT INTO users (email, password, first_name, last_name) VALUES (?, ?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "SELECT user_id, email, password, first_name, last_name FROM users WHERE user_id = ?";
    private static final String UPDATE_USER_SQL = "UPDATE users SET email = ?, password = ?, first_name = ?, last_name = ? WHERE user_id = ?;";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE user_id = ?;";
    private static final String AUTHENTICATE_USER_SQL = "SELECT user_id, email, password, first_name, last_name FROM users WHERE email = ? AND password = ?;";

    @Override
    public void insertUser(User user) throws SQLException {
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
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
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setInt(5, user.getUserId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public boolean deleteUser(int userId) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_SQL)) {
            statement.setInt(1, userId);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public Optional<User> selectUserById(int userId) {
        Optional<User> user = Optional.empty();
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                user = Optional.of(new User(id, email, password, firstName, lastName));
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
        return user;
    }

    @Override
    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> user = Optional.empty();
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(AUTHENTICATE_USER_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String userEmail = rs.getString("email");
                String userPassword = rs.getString("password");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                user = Optional.of(new User(userId, userEmail, userPassword, firstName, lastName));
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
        return user;
    }
}
