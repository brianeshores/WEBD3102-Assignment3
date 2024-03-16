<%--
  Created by IntelliJ IDEA.
  User: brian
  Date: 2024-03-16
  Time: 10:37 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Confirmation</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>order/orderConfirmation.css"> <!-- Adjust the path as necessary -->
</head>
<body>
<div class="content-wrapper">
    <h2>Order Confirmation</h2>
    <p>Your order has been placed successfully!</p>
    <p>Thank you for shopping with us. You will receive a confirmation email shortly.</p>
    <a href="<%=request.getContextPath()%>/product?action=list" class="btn">Continue Shopping</a>
</div>
</body>
</html>

