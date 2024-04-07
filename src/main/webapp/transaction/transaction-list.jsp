<%--
  Created by IntelliJ IDEA.
  User: brian
  Date: 2024-04-01
  Time: 2:44 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Transactions</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/transaction/transaction-list.css">
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
    <h2>Current Balance: $<%= request.getAttribute("balance") %></h2>
    <div class="action-container">
     <a href="transaction?action=insert" class="btn add-button">Add New Transaction</a>
    </div>

    <div class="table-responsive">
        <table class="product-table">
            <thead>
            <tr class="table-header">
                <th>Description</th>
                <th>Amount</th>
                <th>Date</th>
                <th>Category</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <%@ page import="java.util.List" %>
            <%@ page import="com.example.budgetapp.model.Transaction" %>
            <%@ page import="com.example.budgetapp.model.Category" %>
            <%@ page import="com.example.budgetapp.model.User" %>
            <%@ page import="java.text.NumberFormat" %>
            <%@ page import="java.util.Locale" %>
            <%
                List<Transaction> listTransaction = (List<Transaction>) request.getAttribute("listTransaction");
                NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US); // Adjust Locale as necessary

                for (Transaction transaction : listTransaction) {
                    String formattedAmount = formatter.format(transaction.getAmount());

            %>
            <tr>
                <td><%= transaction.getDescription() %></td>
                <td><%= formattedAmount %></td>
                <td><%= transaction.getTransactionDate() %></td>
                <td><%= transaction.getCategoryName() %></td>
                <td class="action-container">
                    <a href="transaction?action=edit&transaction_id=<%= transaction.getTransactionId() %>" class="btn">Edit</a>
                    &nbsp;&nbsp;
                    <a href="transaction?action=delete&transaction_id=<%= transaction.getTransactionId() %>" onclick="return confirm('Are you sure?')" class="btn">Delete</a>
                    <form id="addTransaction<%= transaction.getTransactionId() %>" action="transaction" method="post" style="display:none;">
                        <input type="hidden" name="action" value="addTransaction" />
                        <input type="hidden" name="transaction_id" value="<%= transaction.getTransactionId() %>" />
                    </form>
                </td>
                <% } %>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
