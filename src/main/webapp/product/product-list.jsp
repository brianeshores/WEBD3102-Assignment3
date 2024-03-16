<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product List</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/product/product-list.css"> <!-- Link to external CSS -->
    <%
        // Check if a user is authenticated by looking for the user object in the session
        User user = (User) request.getSession().getAttribute("user");
        boolean isAuthenticated = user != null;
    %>
</head>
<body>
<div class="content-wrapper">
    <h2>Product List</h2>
    <% if(isAuthenticated) { %>
    <a href="product?action=new" class="btn">Add New Product</a>
    <a href="order?action=view" class="btn">View Order</a>
    <% } %>
    <table class="product-table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Category</th>
            <% if(isAuthenticated) { %>
            <th>Actions</th>
            <% } %>
        </tr>
        </thead>
        <tbody>
        <%@ page import="java.util.List" %>
        <%@ page import="com.example.ordermanagement.model.Product" %>
        <%@ page import="com.example.ordermanagement.model.User" %>
        <%
            List<Product> listProduct = (List<Product>) request.getAttribute("listProduct");
            for (Product product : listProduct) {
        %>
        <tr>
            <td><%= product.getName() %></td>
            <td><%= product.getDescription() %></td>
            <td>$<%= product.getPrice() %></td>
            <td><%= product.getQuantity() %></td>
            <td><%= product.getCategory() %></td>
            <% if(isAuthenticated) { %>
            <td>
                <a href="product?action=edit&product_id=<%= product.getProductId() %>" class="btn">Edit</a>
                &nbsp;&nbsp;
                <a href="#" onclick="document.getElementById('addProductForm<%= product.getProductId() %>').submit();" class="btn">Add</a>
                &nbsp;&nbsp;
                <a href="product?action=delete&product_id=<%= product.getProductId() %>" onclick="return confirm('Are you sure?')" class="btn">Delete</a>
                <form id="addProductForm<%= product.getProductId() %>" action="product" method="post" style="display:none;">
                    <input type="hidden" name="action" value="addProduct" />
                    <input type="hidden" name="product_id" value="<%= product.getProductId() %>" />
                </form>
            </td>
            <% } %>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
