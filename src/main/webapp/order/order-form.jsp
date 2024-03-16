<%--
  Created by IntelliJ IDEA.
  User: brian
  Date: 2024-03-11
  Time: 5:51 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Order Form</title>
  <style>
    label, input, select, button {
      display: block;
      margin-top: 10px;
    }
    .container {
      width: 300px;
      padding: 20px;
      border: 1px solid #ccc;
      margin-top: 20px;
    }
  </style>
</head>
<body>
<div class="container">
  <h2>Order Form</h2>
  <form action="order" method="POST">
    <input type="hidden" name="id" value="<%= request.getAttribute("order") != null ? ((com.example.ordermanagement.model.Order)request.getAttribute("order")).getOrderId() : "0" %>" />
    <label for="userId">User ID:</label>
    <input type="text" id="userId" name="userId" value="<%= request.getAttribute("order") != null ? ((com.example.ordermanagement.model.Order)request.getAttribute("order")).getUserId() : "" %>" required />

    <label for="status">Status:</label>
    <select id="status" name="status">
      <option value="Placed" <%= request.getAttribute("order") != null && ((com.example.ordermanagement.model.Order)request.getAttribute("order")).getStatus().equals("Placed") ? "selected" : "" %>>Placed</option>
      <option value="Shipped" <%= request.getAttribute("order") != null && ((com.example.ordermanagement.model.Order)request.getAttribute("order")).getStatus().equals("Shipped") ? "selected" : "" %>>Shipped</option>
      <option value="Delivered" <%= request.getAttribute("order") != null && ((com.example.ordermanagement.model.Order)request.getAttribute("order")).getStatus().equals("Delivered") ? "selected" : "" %>>Delivered</option>
      <option value="Cancelled" <%= request.getAttribute("order") != null && ((com.example.ordermanagement.model.Order)request.getAttribute("order")).getStatus().equals("Cancelled") ? "selected" : "" %>>Cancelled</option>
    </select>

    <% if (request.getAttribute("order") != null) { %>
    <input type="hidden" name="action" value="update" />
    <button type="submit">Update Order</button>
    <% } else { %>
    <input type="hidden" name="action" value="insert" />
    <button type="submit">Place Order</button>
    <% } %>
  </form>
</div>
</body>
</html>

