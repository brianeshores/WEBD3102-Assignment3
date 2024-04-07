<%--
  Created by IntelliJ IDEA.
  User: brian
  Date: 2024-03-11
  Time: 6:59 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <style>
        .login-container {
            width: 300px;
            margin: auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-top: 50px;
        }
        .form-input {
            margin-bottom: 20px;
        }
        .form-input label {
            display: block;
        }
        .form-input input {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
        }
        .form-input button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .form-input button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<div class="login-container">
    <h2>Login</h2>
    <% if (request.getAttribute("message") != null) { %>
    <p style="color: red;"><%= request.getAttribute("message") %></p>
    <% } %>
    <form action="user?action=login" method="post">
        <div class="form-input">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
        </div>
        <div class="form-input">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="form-input">
            <button type="submit">Login</button>
        </div>
    </form>
</div>

</body>
</html>
