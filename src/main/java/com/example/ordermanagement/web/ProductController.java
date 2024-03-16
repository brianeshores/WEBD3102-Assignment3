package com.example.ordermanagement.web;

import com.example.ordermanagement.dao.ProductDao;
import com.example.ordermanagement.db.ProductDB;
import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpSession;

//@WebServlet("/product")
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDao productDao;

    public void init() {
        productDao = new ProductDB();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Here you handle both adding a product to an order and the existing doPost logic
        String action = request.getParameter("action");
        if ("addProduct".equals(action)) {
            // Assuming "addProduct" is the action for adding a product to the order
            addProductToOrder(request, response);
        } else {
            // For other actions, simply forward the request to doGet
            doGet(request, response);
        }
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
                    insertProduct(request, response);
                    break;
                case "delete":
                    deleteProduct(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateProduct(request, response);
                    break;
                case "list":
                    listProducts(request, response);
                    break;
                default:
                    listProducts(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Product> listProduct = productDao.selectAllProducts();
        request.setAttribute("listProduct", listProduct);
        RequestDispatcher dispatcher = request.getRequestDispatcher("product/product-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("product/product-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("product_id")); // Changed from "id" to "product_id"
        Product existingProduct = productDao.selectProduct(productId);
        RequestDispatcher dispatcher = request.getRequestDispatcher("product/product-form.jsp");
        request.setAttribute("product", existingProduct);
        dispatcher.forward(request, response);
    }

    private void insertProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String category = request.getParameter("category");

        Product newProduct = new Product(name, description, price, quantity, category);
        productDao.insertProduct(newProduct);
        response.sendRedirect("product?action=list");
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int productId = Integer.parseInt(request.getParameter("product_id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String category = request.getParameter("category");

        Product productToUpdate = new Product(productId, name, description, price, quantity, category);
        productDao.updateProduct(productToUpdate);
        response.sendRedirect("product?action=list");
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int productId = Integer.parseInt(request.getParameter("product_id"));
        productDao.deleteProduct(productId);
        response.sendRedirect("product?action=list");
    }

    private void addProductToOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("product_id"));
        Product product = null;
        try {
            product = productDao.selectProduct(productId);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving product", e);
        }

        HttpSession session = request.getSession();
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        if (currentOrder == null) {
            currentOrder = new Order();
            session.setAttribute("currentOrder", currentOrder);
        }
        currentOrder.addProduct(product);

        response.sendRedirect("product?action=list");
    }
}
