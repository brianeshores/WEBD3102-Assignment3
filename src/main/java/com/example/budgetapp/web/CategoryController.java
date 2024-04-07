package com.example.budgetapp.web;

import com.example.budgetapp.dao.CategoryDao;
import com.example.budgetapp.db.CategoryDB;
import com.example.budgetapp.model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/category")
public class CategoryController extends HttpServlet {
    private CategoryDao categoryDao;

    public void init() {
        categoryDao = new CategoryDB();
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
                case "insert":
                    insertCategory(request, response);
                    break;
                case "list":
                    listCategories(request, response);
                    break;
                // Implement other cases as needed
                default:
                    listCategories(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Category> listCategory = categoryDao.selectAllCategories();
        request.setAttribute("listCategory", listCategory);
        request.getRequestDispatcher("category/category-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("category/category-form.jsp").forward(request, response);
    }

    private void insertCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String categoryName = request.getParameter("categoryName");
        String type = request.getParameter("type");
        Long id = Long.valueOf(request.getParameter("categoryId"));
        Category newCategory = new Category(id, categoryName, type);
        categoryDao.insertCategory(newCategory);
        response.sendRedirect("category?action=list");
    }
}
