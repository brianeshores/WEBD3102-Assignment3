<%--
  Created by IntelliJ IDEA.
  User: brian
  Date: 2024-04-07
  Time: 8:23 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: brian
  Date: 2024-04-01
  Time: 8:32 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Budgets</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/budget/budget-list.css"> <!-- Adjust the path as necessary -->
</head>
<body>
<nav>
    <ul>
        <li><a href="<%=request.getContextPath()%>/dashboard">Home</a></li>
        <li><a href="<%=request.getContextPath()%>/transaction?action=list">Transactions</a></li>
        <li><a href="<%=request.getContextPath()%>/budget?action=list">Budgets</a></li>
    </ul>
</nav>
<div class="content-wrapper">
    <h2>Your Budgets</h2>
    <div class="action-container">
        <a href="budget?action=insert" class="btn add-button">Add New Budget</a>
    </div>

    <div class="table-responsive">
        <table class="product-table">
            <thead>
            <tr class="table-header">
                <th>Category</th>
                <th>Amount</th>
                <th>Month</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <%@ page import="java.util.List" %>
            <%@ page import="com.example.budgetapp.model.Budget" %>
            <%@ page import="java.text.NumberFormat" %>
            <%@ page import="java.util.Locale" %>
            <%
                List<Budget> listBudget = (List<Budget>) request.getAttribute("listBudget");
                NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US); // Adjust Locale as necessary

                for (Budget budget : listBudget) {
                    String formattedAmount = formatter.format(budget.getAmount());
            %>
            <tr>
                <td><%= budget.getCategoryName() %></td> <!-- Consider fetching category name if available -->
                <td><%= formattedAmount %></td>
                <td><%= budget.getMonth() %></td>
                <td class="action-container">
                    <a href="budget?action=edit&budgetId=<%= budget.getBudgetId() %>" class="btn">Edit</a>
                    &nbsp;&nbsp;
                    <a href="budget?action=delete&budgetId=<%= budget.getBudgetId() %>" onclick="return confirm('Are you sure?')" class="btn">Delete</a>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
