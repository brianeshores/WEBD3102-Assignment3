package com.example.todo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.todo.dao.TodoDao;
import com.example.todo.model.Todo;
import com.example.todo.utils.JDBCUtils;

public class TodoDB implements TodoDao {

    private static final String INSERT_TODOS_SQL = "INSERT INTO todo" +
            "  (title, description, targetDate,  status) VALUES " + " (?, ?, ?, ?, ?);";

    private static final String SELECT_TODO_BY_ID = "SELECT id,title,description,targetDate,status FROM todo WHERE id =?";
    private static final String SELECT_ALL_TODOS = "select * from todo";
    private static final String DELETE_TODO_BY_ID = "delete from todo where id = ?;";
    private static final String UPDATE_TODO = "update todo set title = ?, description =?, targetDate =?, status = ? where id = ?;";

    public TodoDB() {}

    @Override
    public void insertTodo(Todo todo) throws SQLException {
        System.out.println(INSERT_TODOS_SQL);
        // try-with-resource statement will auto close the connection.
        try (Connection connection = JDBCUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TODOS_SQL)) {
            preparedStatement.setString(1, todo.getTitle());
            preparedStatement.setString(3, todo.getDescription());
            preparedStatement.setDate(4, JDBCUtils.getSQLDate(todo.getTargetDate()));
            preparedStatement.setBoolean(5, todo.getStatus());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JDBCUtils.printSQLException(exception);
        }
    }

    @Override
    public Todo selectTodo(long todoId) {
        Todo todo = null;
        // Step 1: Establishing a Connection
        try (Connection connection = JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TODO_BY_ID);) {
            preparedStatement.setLong(1, todoId);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println("response: "+ rs  );
            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                LocalDate targetDate = rs.getDate("targetDate").toLocalDate();
                boolean isDone = rs.getBoolean("status");
                todo = new Todo(id, title, description, targetDate, isDone);
            }
        } catch (SQLException exception) {
            JDBCUtils.printSQLException(exception);
        }
        return todo;
    }

    @Override
    public List < Todo > selectAllTodos() {

        // using try-with-resources to avoid closing resources (boilerplate code)
        List<Todo> todos = new ArrayList<>();

        // Step 1: Establishing a Connection
        LocalDate targetDate = null;
        try (Connection connection = JDBCUtils.getConnection();

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TODOS);) {
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                targetDate = rs.getDate("targetDate").toLocalDate();
                boolean isDone = rs.getBoolean("status");
                todos.add(new Todo(id, title, description, targetDate, isDone));
            }
        } catch (SQLException exception) {
            JDBCUtils.printSQLException(exception);
        }
        return todos;
    }

    @Override
    public boolean deleteTodo(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = JDBCUtils.getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_TODO_BY_ID);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean updateTodo(Todo todo) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = JDBCUtils.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_TODO);) {
            statement.setString(1, todo.getTitle());
            statement.setString(2, todo.getDescription());
            statement.setDate(3, JDBCUtils.getSQLDate(todo.getTargetDate()));
            statement.setBoolean(4, todo.getStatus());
            statement.setLong(5, todo.getId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
}
