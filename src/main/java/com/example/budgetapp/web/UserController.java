package com.example.budgetapp.web;

import com.example.budgetapp.dao.UserDao;
import com.example.budgetapp.db.UserDB;
import com.example.budgetapp.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user")
public class UserController extends HttpServlet {
    private UserDao userDao;

    public void init() {
        userDao = new UserDB();
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
                case "login":
                    authenticateUser(request, response);
                    break;
                case "register":
                    registerUser(request, response);
                    break;
                case "logout":
                    logoutUser(request, response);
                    break;
                // Add more cases as needed
                default:
                    response.sendRedirect("login/login.jsp");
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String userName = request.getParameter("userName");
        String email = request.getParameter("email");
        String password = request.getParameter("password"); // Consider hashing the password

        User newUser = new User(userName, email, password);
        userDao.insertUser(newUser);
        response.sendRedirect("login/login.jsp"); // Redirect to login page after registration
    }

    private void authenticateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("password"); // Consider hashing the password for comparison

        User user = userDao.authenticateUser(email, password).orElse(null);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("message", "Invalid email/password");
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }
}

