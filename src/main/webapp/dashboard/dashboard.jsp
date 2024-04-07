<%--
  Created by IntelliJ IDEA.
  User: brian
  Date: 2024-04-07
  Time: 8:32 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.budgetapp.model.Transaction, com.example.budgetapp.model.Budget" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<html>
<head>
  <title>Dashboard</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/dashboard/dashboard.css">
</head>
<body>
<!-- Navigation Links -->
<nav>
  <ul>
    <li><a href="<%=request.getContextPath()%>/dashboard">Home</a></li>
    <li><a href="<%=request.getContextPath()%>/transaction?action=list">Transactions</a></li>
    <li><a href="<%=request.getContextPath()%>/budget?action=list">Budgets</a></li>
  </ul>
</nav>
<div class="main-content">
  <div class="latest-transactions">
    <h3>Latest Transactions</h3>
    <ul>
      <% List<Transaction> transactions = (List<Transaction>) request.getAttribute("latestTransactions");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        if (transactions != null) {
          for (Transaction transaction : transactions) {
            String formattedAmount = formatter.format(transaction.getAmount());
      %>
      <li>
        <%= transaction.getDescription() %> - <%= formattedAmount %> - <%= transaction.getTransactionDate() %>
      </li>
      <%      }
      } %>
    </ul>
    <a href="<%=request.getContextPath()%>/transaction?action=list" class="btn">See All</a>
  </div>
  <div class="budgets">
    <h3>Budgets</h3>
    <ul>
      <% List<Budget> budgets = (List<Budget>) request.getAttribute("latestBudgets");
        if (budgets != null) {
          for (Budget budget : budgets) {
            // Example percentage calculation; adjust according to your needs
            double percentage = 50; // Placeholder value
      %>
      <li>
        <span><%=  budget.getCategoryName() %> - <%= budget.getMonth() %> - <%= budget.getUtilizationPercentage()%> - <%= formatter.format(budget.getAmount()) %></span>
        <div class="progress-bar">
          <div class="progress" style="width: <%= budget.getUtilizationPercentage() %>%; background-color: #4CAF50;"></div>
        </div>
      </li>
      <%      }
      } %>
    </ul>
    <a href="<%=request.getContextPath()%>/budget?action=list" class="btn">See All</a>
  </div>
</div>
</body>
</html>

