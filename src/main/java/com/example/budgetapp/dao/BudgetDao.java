package com.example.budgetapp.dao;

import com.example.budgetapp.model.Budget;
import java.sql.SQLException;
import java.util.List;

public interface BudgetDao {
    void insertBudget(Budget budget) throws SQLException;
    boolean updateBudget(Budget budget) throws SQLException;
    boolean deleteBudget(long budgetId) throws SQLException;
    List<Budget> selectAllBudgetsByUserId(long userId);
    Budget selectBudgetById(long budgetId);
}
