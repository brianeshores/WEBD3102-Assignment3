package com.example.budgetapp.web;

import com.example.budgetapp.dao.BudgetDao;
import com.example.budgetapp.dao.CategoryDao;
import com.example.budgetapp.dao.TransactionDao;
import com.example.budgetapp.db.BudgetDB;
import com.example.budgetapp.db.CategoryDB;
import com.example.budgetapp.db.TransactionDB;
import com.example.budgetapp.model.Budget;
import com.example.budgetapp.model.Category;
import com.example.budgetapp.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/budget")
public class BudgetController extends HttpServlet {
    private BudgetDao budgetDao;
    private TransactionDao transactionDao;

    public void init() {
        budgetDao = new BudgetDB();
        transactionDao = new TransactionDB();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteBudget(request, response);
                    break;
                case "list":
                    listBudgets(request, response);
                    break;
                case "insert":
                    showNewForm(request, response);
                    break;
                case "update":
                    updateBudget(request, response);
                    break;
                case "add":
                    insertBudget(request, response);
                    break;
                default:
                    listBudgets(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listBudgets(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            long userId = user.getUserId();
            // Assuming transactionDao is an instance of a class implementing TransactionDao, and it's available in this context
            Map<Long, Double> totalSpentPerCategory = transactionDao.sumTransactionsByCategoryAndMonth(userId);

            List<Budget> listBudget = budgetDao.selectAllBudgetsByUserId(userId);

            for (Budget budget : listBudget) {
                Double totalSpent = totalSpentPerCategory.getOrDefault(budget.getCategoryId(), 0.0);
                double budgetAmount = budget.getAmount();
                double utilizationPercentage = (totalSpent / budgetAmount) * 100;
                System.out.println("1: " + utilizationPercentage);
                if(utilizationPercentage > 100) {
                    utilizationPercentage = 100;
                }
                System.out.println("2: " + utilizationPercentage);

                // Check if you need to handle cases where budgetAmount is 0 to avoid division by zero
                if (budgetAmount == 0) {
                    utilizationPercentage = 0; // Or handle accordingly
                }

                budget.setUtilizationPercentage(utilizationPercentage); // Set the calculated percentage
            }

            request.setAttribute("listBudget", listBudget);
            RequestDispatcher dispatcher = request.getRequestDispatcher("budget/budget-list.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }


    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDao categoryDao = new CategoryDB();
        List<Category> listCategory = categoryDao.selectAllCategories();
        request.setAttribute("listCategory", listCategory);
        RequestDispatcher dispatcher = request.getRequestDispatcher("budget/budget-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        long budgetId = Long.parseLong(request.getParameter("budgetId"));
        Budget existingBudget = budgetDao.selectBudgetById(budgetId);
        CategoryDao categoryDao = new CategoryDB();
        List<Category> listCategory = categoryDao.selectAllCategories();
        request.setAttribute("budget", existingBudget);
        request.setAttribute("listCategory", listCategory);
        RequestDispatcher dispatcher = request.getRequestDispatcher("budget/budget-form.jsp");
        dispatcher.forward(request, response);
    }

    private void insertBudget(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        // Retrieval and parsing of request parameters for Budget creation
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        long userId = user.getUserId();
        long categoryId = Long.parseLong(request.getParameter("categoryId"));
        double amount = Double.parseDouble(request.getParameter("amount"));
        String month = request.getParameter("month");

        Budget newBudget = new Budget(0, userId, categoryId, amount, month); // BudgetID is auto-generated
        budgetDao.insertBudget(newBudget);
        response.sendRedirect("budget?action=list");
    }

    private void updateBudget(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long budgetId = Long.parseLong(request.getParameter("budgetId"));
        long categoryId = Long.parseLong(request.getParameter("categoryId"));
        double amount = Double.parseDouble(request.getParameter("amount"));
        String month = request.getParameter("month");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        long userId = user.getUserId();

        Budget budgetToUpdate = new Budget(budgetId, userId, categoryId, amount, month);
        budgetDao.updateBudget(budgetToUpdate);
        response.sendRedirect("budget?action=list");
    }

    private void deleteBudget(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long budgetId = Long.parseLong(request.getParameter("budgetId"));
        budgetDao.deleteBudget(budgetId);
        response.sendRedirect("budget?action=list");
    }
}
