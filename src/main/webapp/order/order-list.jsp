<%--
  Created by IntelliJ IDEA.
  User: brian
  Date: 2024-03-11
  Time: 5:17 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order List</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
        }
        th {
            text-align: left;
        }
    </style>
</head>
<body>
<h2>Order List</h2>
<a href="order?action=new">Add New Order</a>
<table>
    <thead>
    <tr>
        <th>Order ID</th>
        <th>User ID</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <%@ page import="java.util.List" %>
    <%@ page import="com.example.ordermanagement.model.Order" %>
    <%
        List<Order> listOrder = (List<Order>) request.getAttribute("listOrder");
        for (Order order : listOrder) {
    %>
    <tr>
        <td><%= order.getOrderId() %></td>
        <td><%= order.getUserId() %></td>
        <td><%= order.getStatus() %></td>
        <td>
            <a href="order?action=edit&id=<%= order.getOrderId() %>">Edit</a>
            &nbsp;&nbsp;
            <a href="order?action=delete&id=<%= order.getOrderId() %>" onclick="return confirm('Are you sure?')">Delete</a>
            &nbsp;&nbsp;
            <a href="order?id=<%= order.getOrderId() %>">Details</a>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
</body>
</html>

