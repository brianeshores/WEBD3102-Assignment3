package com.example.budgetapp.db;

import com.example.budgetapp.dao.CategoryDao;
import com.example.budgetapp.dao.TransactionDao;
import com.example.budgetapp.model.Category;
import com.example.budgetapp.model.Transaction;
import com.example.budgetapp.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionDB implements TransactionDao {

    private static final String INSERT_TRANSACTION_SQL = "INSERT INTO Transactions (UserID, CategoryID, Amount, TransactionDate, Description) VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE_TRANSACTION_SQL = "UPDATE Transactions SET UserID = ?, CategoryID = ?, Amount = ?, TransactionDate = ?, Description = ? WHERE TransactionID = ?;";
    private static final String DELETE_TRANSACTION_SQL = "DELETE FROM Transactions WHERE TransactionID = ?;";
    private static final String SELECT_ALL_TRANSACTIONS_BY_USER_SQL =
            "SELECT t.*, c.categoryName FROM Transactions t " +
                    "JOIN Categories c ON t.CategoryID = c.CategoryID " +
                    "WHERE t.UserID = ? " +
                    "ORDER BY t.TransactionDate DESC;"; // Or DESC for descending order

    private static final String SELECT_TRANSACTION_BY_ID_SQL =
            "SELECT t.TransactionID, t.UserID, t.CategoryID, t.Amount, t.TransactionDate, t.Description, c.CategoryName " +
                    "FROM Transactions t " +
                    "JOIN Categories c ON t.CategoryID = c.CategoryID " +
                    "WHERE t.TransactionID = ?";

    @Override
    public void insertTransaction(Transaction transaction) throws SQLException {
        // Fetch the category
        CategoryDao categoryDao = new CategoryDB(); // Or however you normally obtain an instance of CategoryDao
        Category category = categoryDao.selectCategoryById(transaction.getCategoryId());

        // Check the category type and adjust the amount
        double amount = "Expense".equals(category.getType()) ? -Math.abs(transaction.getAmount()) : Math.abs(transaction.getAmount());

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION_SQL)) {
            preparedStatement.setLong(1, transaction.getUserId());
            preparedStatement.setLong(2, transaction.getCategoryId());
            preparedStatement.setDouble(3, amount);
            preparedStatement.setDate(4, new java.sql.Date(transaction.getTransactionDate().getTime()));
            preparedStatement.setString(5, transaction.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting transaction: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Sums up transactions by category for a specific user and month.
     *
     * @param userId The user ID.
     * @return A map with CategoryID as the key and the sum of transaction amounts as the value.
     */
    public Map<Long, Double> sumTransactionsByCategoryAndMonth(long userId) {
        Map<Long, Double> totalSpentPerCategory = new HashMap<>();
        String SQL_SUM_BY_CATEGORY =
                "SELECT CategoryID, SUM(ABS(Amount)) AS TotalAmount " + // Use ABS to get the absolute value
                        "FROM Transactions " +
                        "WHERE UserID = ? " +
                        "GROUP BY CategoryID;";

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SUM_BY_CATEGORY)) {
            preparedStatement.setLong(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long categoryId = rs.getLong("CategoryID");
                double totalAmount = rs.getDouble("TotalAmount"); // Now the total amount is always positive
                totalSpentPerCategory.put(categoryId, Math.abs(totalAmount)); // Ensure the value is positive
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }

        return totalSpentPerCategory;
    }


    @Override
    public boolean updateTransaction(Transaction transaction) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TRANSACTION_SQL)) {
            preparedStatement.setLong(1, transaction.getUserId());
            preparedStatement.setLong(2, transaction.getCategoryId());
            preparedStatement.setDouble(3, transaction.getAmount());
            preparedStatement.setDate(4, new java.sql.Date(transaction.getTransactionDate().getTime()));
            preparedStatement.setString(5, transaction.getDescription());
            preparedStatement.setLong(6, transaction.getTransactionId());

            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }



    @Override
    public boolean deleteTransaction(long transactionId) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TRANSACTION_SQL)) {
            preparedStatement.setLong(1, transactionId);

            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public List<Transaction> selectAllTransactionsByUserId(long userId) {
        System.out.println("Listing transactions");
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TRANSACTIONS_BY_USER_SQL)) {
            preparedStatement.setLong(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long transactionId = rs.getLong("TransactionID");
                double amount = rs.getDouble("Amount");
                Date transactionDate = rs.getDate("TransactionDate");
                String description = rs.getString("Description");
                String categoryName = rs.getString("CategoryName"); // Get category name from result set
                // Assuming you have a constructor or setter in Transaction to handle category name
                transactions.add(new Transaction(transactionId, userId, categoryName, amount,  transactionDate, description));
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
        return transactions;
    }

    @Override
    public Transaction selectTransactionById(long transactionId) {
        Transaction transaction = null;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRANSACTION_BY_ID_SQL)) {
            preparedStatement.setLong(1, transactionId);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println("selecting category: ");
            if (rs.next()) {
                long userId = rs.getLong("UserID");
                String categoryName = rs.getString("CategoryName");
                double amount = rs.getDouble("Amount");
                Date transactionDate = rs.getDate("TransactionDate");
                String description = rs.getString("Description");

                transaction = new Transaction(transactionId, userId, categoryName, amount, transactionDate, description);
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
        return transaction;
    }
}
