package com.example.budgetapp.dao;

import com.example.budgetapp.model.Transaction;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface TransactionDao {
    void insertTransaction(Transaction transaction) throws SQLException;
    boolean updateTransaction(Transaction transaction) throws SQLException;
    boolean deleteTransaction(long transactionId) throws SQLException;
    List<Transaction> selectAllTransactionsByUserId(long userId);
    Transaction selectTransactionById(long transactionId);

    Map<Long, Double> sumTransactionsByCategoryAndMonth(long userId);

}
