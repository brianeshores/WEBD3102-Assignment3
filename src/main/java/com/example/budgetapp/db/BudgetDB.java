package com.example.budgetapp.db;

import com.example.budgetapp.dao.BudgetDao;
import com.example.budgetapp.dao.CategoryDao;
import com.example.budgetapp.model.Budget;
import com.example.budgetapp.model.Category;
import com.example.budgetapp.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDB implements BudgetDao {

    private static final String INSERT_BUDGET_SQL = "INSERT INTO budgets (UserID, CategoryID, Amount, Month) VALUES (?, ?, ?, ?);";
    private static final String UPDATE_BUDGET_SQL = "UPDATE budgets SET UserID = ?, CategoryID = ?, Amount = ?, Month = ? WHERE BudgetID = ?;";
    private static final String DELETE_BUDGET_SQL = "DELETE FROM budgets WHERE BudgetID = ?;";
    private static final String SELECT_ALL_BUDGETS_BY_USER_SQL =
            "SELECT budgets.*, categories.CategoryName FROM budgets " +
                    "INNER JOIN categories ON budgets.CategoryID = categories.CategoryID " +
                    "WHERE budgets.UserID = ? ORDER BY budgets.Month DESC;";

    @Override
    public void insertBudget(Budget budget) throws SQLException {
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BUDGET_SQL)) {
            preparedStatement.setLong(1, budget.getUserId());
            preparedStatement.setLong(2, budget.getCategoryId());
            preparedStatement.setDouble(3, budget.getAmount());
            preparedStatement.setString(4, budget.getMonth());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
            throw e;
        }
    }

    @Override
    public boolean updateBudget(Budget budget) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BUDGET_SQL)) {
            preparedStatement.setLong(1, budget.getUserId());
            preparedStatement.setLong(2, budget.getCategoryId());
            preparedStatement.setDouble(3, budget.getAmount());
            preparedStatement.setString(4, budget.getMonth());
            preparedStatement.setLong(5, budget.getBudgetId());

            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public boolean deleteBudget(long budgetId) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BUDGET_SQL)) {
            preparedStatement.setLong(1, budgetId);

            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public List<Budget> selectAllBudgetsByUserId(long userId) {
        List<Budget> budgets = new ArrayList<>();
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BUDGETS_BY_USER_SQL)) {
            preparedStatement.setLong(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long budgetId = rs.getLong("BudgetID");
                long categoryId = rs.getLong("CategoryID");
                double amount = rs.getDouble("Amount");
                String month = rs.getString("Month");
                String categoryName = rs.getString("CategoryName"); // Fetching the CategoryName
                // Adjusted Budget constructor to accept categoryName
                budgets.add(new Budget(budgetId, userId, categoryId, amount, month, categoryName));
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
        return budgets;
    }


    @Override
    public Budget selectBudgetById(long budgetId) {
        Budget budget = null;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BUDGETS_BY_USER_SQL)) {
            preparedStatement.setLong(1, budgetId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("UserID");
                int categoryId = rs.getInt("CategoryID");
                double amount = rs.getDouble("Amount");
                String month = rs.getString("Month");
                String categoryName = rs.getString("CategoryName");
                budget = new Budget(budgetId, userId, categoryId, amount, month, categoryName);
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
        return budget;
    }
}
