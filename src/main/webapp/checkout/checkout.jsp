<%--
  Created by IntelliJ IDEA.
  User: brian
  Date: 2024-03-12
  Time: 5:25 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Checkout</title>
</head>
<body>
<h2>Checkout</h2>
<form action="finalizeOrder" method="post">
    Name: <input type="text" name="name"/><br/>
    Address: <input type="text" name="address"/><br/>
    Email: <input type="email" name="email"/><br/>
    <input type="submit" value="Submit Order"/>
</form>
</body>
</html>
