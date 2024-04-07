package com.example.budgetapp.web;

import com.example.budgetapp.dao.CategoryDao;
import com.example.budgetapp.dao.TransactionDao;
import com.example.budgetapp.db.CategoryDB;
import com.example.budgetapp.db.TransactionDB;
import com.example.budgetapp.model.Category;
import com.example.budgetapp.model.Transaction;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/transaction")
public class TransactionController extends HttpServlet {
    private TransactionDao transactionDao;

    public void init() {
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
                    deleteTransaction(request, response);
                    break;
                case "list":
                    listTransactions(request, response);
                    break;
                case "insert":
                    showNewForm(request, response);
                    break;
                case "update":
                    updateTransaction(request, response);
                    break;
                case "add":
                    insertTransaction(request, response);
                    break;
                default:
                    listTransactions(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listTransactions(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            long userId = user.getUserId();
            List<Transaction> listTransaction = transactionDao.selectAllTransactionsByUserId(userId);
            System.out.println("all transaction " + listTransaction);

            // Assuming listTransaction is already sorted by date DESC
            // Extract the first 5 transactions for the latestTransactions
            List<Transaction> latestTransactions = listTransaction.size() > 5 ?
                    listTransaction.subList(0, 5) :
                    new ArrayList<>(listTransaction);
            // Calculate balance
            double balance = listTransaction.stream()
                    .mapToDouble(Transaction::getAmount)
                    .sum();

            // Format balance to 2 decimal places as a string
            String formattedBalance = String.format("%.2f", balance);

            // Set attributes for both the complete list and the latest transactions
            request.setAttribute("listTransaction", listTransaction);
            request.setAttribute("latestTransactions", latestTransactions); // New attribute for the latest transactions
            request.setAttribute("balance", formattedBalance); // Use the formatted balance string

            // Forward to the transaction list page (adjust if using for dashboard)
            request.getRequestDispatcher("transaction/transaction-list.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }




    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDao categoryDao = new CategoryDB(); // Initialize your CategoryDAO
        List<Category> listCategory = categoryDao.selectAllCategories(); // Fetch categories
        request.setAttribute("listCategory", listCategory); // Set fetched categories as a request attribute
        request.getRequestDispatcher("transaction/transaction-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        // Assuming you have a constructor or some initialization code that creates an instance of CategoryDB
        CategoryDao categoryDao = new CategoryDB(); // Create an instance of your CategoryDao implementation

        int transactionId = Integer.parseInt(request.getParameter("transaction_id"));
        Transaction existingTransaction = transactionDao.selectTransactionById(transactionId);

        // Call selectAllCategories on the instance of CategoryDB, not statically
        List<Category> listCategory = categoryDao.selectAllCategories();

        request.setAttribute("transaction", existingTransaction);
        request.setAttribute("listCategory", listCategory); // Set the list of categories as a request attribute

        RequestDispatcher dispatcher = request.getRequestDispatcher("transaction/transaction-form.jsp");
        dispatcher.forward(request, response);
    }

    private void insertTransaction(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String description = request.getParameter("description");
        double amount = Double.parseDouble(request.getParameter("amount"));
        String dateString = request.getParameter("transactionDate");
        java.sql.Date transactionDate = java.sql.Date.valueOf(dateString);
        long categoryId = Long.parseLong(request.getParameter("categoryId"));
        // Assuming User ID is stored in session (adjust based on your application's user management)
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        long userId = user.getUserId();

        Transaction newTransaction = new Transaction(userId, categoryId, amount, (java.sql.Date) transactionDate, description);
        transactionDao.insertTransaction(newTransaction);
        response.sendRedirect("transaction?action=list");
    }

    private void updateTransaction(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        System.out.println("Update method");

        // Retrieve transaction parameters
        long transactionId = Long.parseLong(request.getParameter("transaction_id"));
        String description = request.getParameter("description");
        double amount = Double.parseDouble(request.getParameter("amount"));
        java.sql.Date transactionDate = java.sql.Date.valueOf(request.getParameter("transactionDate"));
        long categoryId = Long.parseLong(request.getParameter("categoryId"));
        String categoryType = request.getParameter("categoryType"); // Retrieve category type from the form

        // Retrieve user from session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        long userId = user.getUserId();

        // Update category
        CategoryDao categoryDao = new CategoryDB();
        Category category = categoryDao.selectCategoryById(categoryId);
        category.setType(categoryType);
        boolean categoryUpdated = categoryDao.updateCategory(category);
        if (!categoryUpdated) {
            System.out.println("Failed to update category for CategoryID: " + categoryId);
            return;
        }

        amount = "Expense".equals(categoryType) ? -Math.abs(amount) : Math.abs(amount);
        Transaction transactionToUpdate = new Transaction(transactionId, userId, categoryId, amount, transactionDate, description);

        boolean transactionUpdated = transactionDao.updateTransaction(transactionToUpdate);
        if (!transactionUpdated) {
            System.out.println("Failed to update transaction for TransactionID: " + transactionId);
        }

        response.sendRedirect("transaction?action=list");
    }


    private long findOrCreateCategory(String categoryName, String type, CategoryDao categoryDao) throws SQLException {
        Category category = categoryDao.findByNameAndType(categoryName, type);
        if (category == null) {
            // Category does not exist, create a new one
            category = new Category();
            category.setCategoryName(categoryName);
            category.setType(type);
            return categoryDao.insertCategory(category);
        } else {
            // Category exists
            return category.getCategoryId();
        }
    }

        private void deleteTransaction(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {
            long transactionId = Long.parseLong(request.getParameter("transaction_id"));
            transactionDao.deleteTransaction(transactionId);
            response.sendRedirect("transaction?action=list");
        }
}
