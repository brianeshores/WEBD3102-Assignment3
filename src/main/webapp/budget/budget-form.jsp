<%--
  Created by IntelliJ IDEA.
  User: brian
  Date: 2024-04-07
  Time: 9:34 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.example.budgetapp.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title><%=request.getAttribute("budget") != null ? "Edit Budget" : "Add New Budget"%></title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/budget/budget-form.css"> <!-- Ensure you have this CSS file -->
</head>
<body>
<nav>
  <ul>
    <li><a href="<%=request.getContextPath()%>/dashboard">Home</a></li>
    <li><a href="<%=request.getContextPath()%>/transaction?action=list">Transactions</a></li>
    <li><a href="<%=request.getContextPath()%>/budget?action=list">Budgets</a></li>
  </ul>
</nav>
<div class="form-wrapper">
  <h2><%=request.getAttribute("budget") != null ? "Edit Budget" : "Add New Budget"%></h2>
  <form action="<%=request.getContextPath()%>/budget" method="post" class="budget-form"> <!-- Update action URL -->
    <% com.example.budgetapp.model.Budget budget = null;
      if (request.getAttribute("budget") != null) {
        budget = (com.example.budgetapp.model.Budget) request.getAttribute("budget");
      }
    %>
    <input type="hidden" name="budgetId" value="<%=budget != null ? budget.getBudgetId() : "" %>" />

    <!-- Removed description as it's not typically part of a budget entity -->

    <div class="form-group">
      <label for="amount">Amount:</label>
      <input type="number" step="0.01" id="amount" name="amount" class="form-control" value="<%=budget != null ? budget.getAmount() : "" %>" required>
    </div>

    <div class="form-group">
      <label for="month">Month:</label>
      <input type="month" id="month" name="month" class="form-control" value="<%=budget != null ? budget.getMonth() : "" %>" required>
    </div>

    <div class="form-group">
      <label for="categoryId">Category:</label>
      <select id="categoryId" name="categoryId" class="form-control" required>
        <% List<Category> categories = (List<Category>) request.getAttribute("listCategory");
          if(categories != null) {
            for(Category category : categories) {
        %>
        <option value="<%= category.getCategoryId() %>" <%= budget != null && budget.getCategoryId() == category.getCategoryId() ? "selected" : "" %>><%= category.getCategoryName() %></option>
        <%
            }
          }
        %>
      </select>
    </div>

    <!-- Removed the category type section as it's not typically part of a budget entity -->

    <button type="submit" class="btn" name="action" value="<%=budget != null ? "update" : "add"%>"><%=budget != null ? "Update" : "Add"%></button>
    <button type="button" class="btn" onclick="window.location.href='<%=request.getContextPath()%>/budget?action=list'">Back to Budgets</button>
  </form>
</div>
</body>
</html>

