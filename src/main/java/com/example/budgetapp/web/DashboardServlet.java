package com.example.budgetapp.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import com.example.budgetapp.dao.TransactionDao;
import com.example.budgetapp.dao.BudgetDao;
import com.example.budgetapp.db.TransactionDB;
import com.example.budgetapp.db.BudgetDB;
import com.example.budgetapp.model.Transaction;
import com.example.budgetapp.model.Budget;
import com.example.budgetapp.model.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {
    private TransactionDao transactionDao;
    private BudgetDao budgetDao;

    public void init() {
        transactionDao = new TransactionDB(); // Initialize your TransactionDao here
        budgetDao = new BudgetDB(); // Initialize your BudgetDao here
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            long userId = user.getUserId();

            // Fetch the latest transactions
            List<Transaction> latestTransactions = transactionDao.selectAllTransactionsByUserId(userId).stream()
                    .limit(5)
                    .collect(Collectors.toList());

            // Fetch and calculate utilization for the latest budgets
            List<Budget> rawBudgets = budgetDao.selectAllBudgetsByUserId(userId);
            Map<Long, Double> totalSpentPerCategory = transactionDao.sumTransactionsByCategoryAndMonth(userId);

            // Apply the limit after setting utilization percentage
            List<Budget> latestBudgets = rawBudgets.stream()
                    .peek(budget -> {
                        Double totalSpent = totalSpentPerCategory.getOrDefault(budget.getCategoryId(), 0.0);
                        double utilizationPercentage = (totalSpent / budget.getAmount()) * 100;
                        if(utilizationPercentage > 100) {
                            utilizationPercentage = 100;
                        }
                        budget.setUtilizationPercentage(utilizationPercentage); // Assuming Budget has this setter
                    })
                    .limit(5)
                    .collect(Collectors.toList());

            // Set attributes for dashboard.jsp
            request.setAttribute("latestTransactions", latestTransactions);
            request.setAttribute("latestBudgets", latestBudgets);

            // Forward to dashboard.jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("dashboard/dashboard.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("login.jsp"); // Redirect to login if user is not logged in
        }
    }
}
