package com.example.ordermanagement.web;

import com.example.ordermanagement.dao.OrderDao;
import com.example.ordermanagement.dao.ProductDao;
import com.example.ordermanagement.db.OrderDB;
import com.example.ordermanagement.db.ProductDB;
import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.model.Product;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/order")
public class OrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDao orderDao;

    public void init() {
        orderDao = new OrderDB();
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
                    insertOrder(request, response);
                    break;
                case "delete":
                    deleteOrder(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateOrderStatus(request, response);
                    break;
                case "list":
                    listOrders(request, response);
                    break;
                case "view":
                    viewOrder(request, response);
                    break;
                case "removeProduct":
                    removeProductFromOrder(request, response);
                    break;
                case "checkout":
                    checkoutOrder(request, response);
                    break;
                default:
                    listOrders(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listOrders(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Order> listOrder = orderDao.selectAllOrders();
        request.setAttribute("listOrder", listOrder);
        RequestDispatcher dispatcher = request.getRequestDispatcher("order/order-list.jsp");
        dispatcher.forward(request, response);
    }

    private void viewOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Order currentOrder = (Order) session.getAttribute("currentOrder");

        if (currentOrder == null) {
            currentOrder = new Order(); // Or handle this scenario differently
            session.setAttribute("currentOrder", currentOrder);
        }

        request.setAttribute("currentOrder", currentOrder);
        RequestDispatcher dispatcher = request.getRequestDispatcher("order/order.jsp");
        dispatcher.forward(request, response);
    }

    private void checkoutOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        ProductDao productDao = new ProductDB(); // Assuming this is your DAO implementation

        if (currentOrder != null && currentOrder.getProductCounts() != null && !currentOrder.getProductCounts().isEmpty()) {
            try {
                for (Map.Entry<Integer, Integer> entry : currentOrder.getProductCounts().entrySet()) {
                    int productId = entry.getKey();
                    int quantityToDeduct = entry.getValue();

                    Product product = productDao.selectProduct(productId); // Fetch the product by its ID
                    if (product != null) {
                        int newQuantity = product.getQuantity() - quantityToDeduct;
                        if (newQuantity < 0) {
                            throw new ServletException("Not enough stock for product ID: " + productId);
                        } else {
                            productDao.updateProductQuantity(productId, newQuantity); // Update the product quantity in the database
                        }
                    }
                }

                // Clear the current order and redirect to confirmation page
                session.removeAttribute("currentOrder");
                response.sendRedirect("order/orderConfirmation.jsp");
            } catch (SQLException e) {
                throw new ServletException("Database error updating product quantities", e);
            }
        } else {
            response.sendRedirect("errorPage.jsp");
        }
    }



    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("order/order-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Order existingOrder = orderDao.selectOrder(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("order/order-form.jsp");
        request.setAttribute("order", existingOrder);
        dispatcher.forward(request, response);
    }

    private void insertOrder(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String status = request.getParameter("status");
        Order newOrder = new Order(0, userId, status);
        orderDao.insertOrder(newOrder);
        response.sendRedirect("order?action=list");
    }

    private void removeProductFromOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        int productIdToRemove = Integer.parseInt(request.getParameter("product_id"));
        currentOrder.getProducts().removeIf(product -> product.getProductId() == productIdToRemove);

        session.setAttribute("currentOrder", currentOrder);
        response.sendRedirect("order?action=view");
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String status = request.getParameter("status");
        Order orderToUpdate = new Order(id, 0, status); // Assuming we're only updating status; adjust as needed
        orderDao.updateOrderStatus(orderToUpdate);
        response.sendRedirect("order?action=list");
    }

    private void deleteOrder(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        orderDao.deleteOrder(id);
        response.sendRedirect("order?action=list");
    }
}
