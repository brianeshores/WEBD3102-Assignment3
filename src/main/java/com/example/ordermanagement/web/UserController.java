package com.example.ordermanagement.web;

import com.example.ordermanagement.dao.UserDao;
import com.example.ordermanagement.db.UserDB;
import com.example.ordermanagement.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

//@WebServlet("/user")
public class UserController extends HttpServlet {
    private UserDao userDao;

    public void init() {
        userDao = new UserDB();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("login".equals(action)) {
            authenticateUser(request, response);
        } // Add other actions like register, update, delete, etc.
    }

    private void authenticateUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Optional<User> authenticatedUser = userDao.authenticateUser(email, password);
        if (authenticatedUser.isPresent()) {
            // User is authenticated
            // For example, redirect to the user's order list or dashboard
            request.getSession().setAttribute("user", authenticatedUser.get());
            response.sendRedirect("product?action=list");
        } else {
            // Authentication failed
            request.setAttribute("message", "Invalid email or password.");
            request.getRequestDispatcher("login/login.jsp").forward(request, response);
        }
    }
}
